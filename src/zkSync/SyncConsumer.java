package zkSync;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.ndnNode.Consumer;
import com.ndnNode.Cs;
import com.ndnNode.Router;
import com.ndnPackage.Data;
import com.ndnPackage.Interest;
import com.ndnPackage.Name;
import com.utils.WRLock;
import com.utils.WRLockImp;

public class SyncConsumer extends Consumer {
	public static final int LEADER = 1;
	public static final int FOLLOWER = 0;
	public static final String NEWDATAFLAG = "/newData/";
	public static final String SYNCFLAG = "/#sync/";
	public static final String NOWDIGESTFLAG = "/#nowDigest/";
	public static final String NEWDIGESTFLAG = "/#newDigest/";
	public static Set<String> allRouterName;
	
	public Map<String,WRLock> lockMap = new ConcurrentHashMap<String,WRLock>();
	public DigestLog digestLog=new DigestLog();
	public List<String> lockList = new LinkedList<String>();
	public String currentDigest;
	private BlockingQueue requestQueue;
	private Data tempData;
	private Integer sConsumerType = FOLLOWER;
	private Set<String> prefixSet = new HashSet<String>();	
	
	public SyncConsumer(int address, HashSet<Interest> requestList, String uniqName) {
		super(address, requestList, uniqName);
		this.getPrefixSet().add(uniqName);
		// TODO Auto-generated constructor stub
	}

	public Integer getsConsumerType() {
		return sConsumerType;
	}

	public void setsConsumerType(Integer sConsumerType) {
		this.sConsumerType = sConsumerType;
	}

	public static int getFollower() {
		return FOLLOWER;
	}

	public void init(){
	}
	
	public SyncConsumer(){
		init();
	}
	
	public SyncConsumer(int address, HashSet<Interest> requestList) {
		super(address, requestList);
		init();
		// TODO Auto-generated constructor stub
	}
	public Data getTempData() {
		return tempData;
	}
	public void setTempData(Data tempData) {
		this.tempData = tempData;
	}
	
	@Override
	public void receive(Data data, int srcAddr) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			this.match(data);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.receive(data, srcAddr);
	}

	@Override
	public void receive(Interest interest) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("router" + this.getAddress() + " receive interest " + interest.getName() + " from "+ interest.getSrcAddr());
		Data data=this.getDataWithCs(interest.getName());
		int srcAddr = interest.getSrcAddr();
		Router router = allRouter.get(srcAddr);
		Interest forwardInterest = new Interest(interest);
		forwardInterest.setSrcAddr(this.getAddress());
		if(data!=null){
			if(router!=null){
				try {
					Data forwardData = (Data)data.clone();
					forwardData.setName(interest.getName());
					this.forward(router, forwardData);
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			if(this.getPit().get(interest)==null){
				this.getPit().add(interest, router);
				HashSet<Router> routerList = this.getFib().get(interest);
				if (routerList != null){
					Iterator<Router> it = routerList.iterator();
					while(it.hasNext()){
						Router nextRouter = it.next();
						this.forward(nextRouter, forwardInterest);
					}
				}
			}
			this.getPit().add(interest, router);
		}
//		System.out.println(interest.getInterestType());
		//判断是否为新数据生成数据包
		if(this.getsConsumerType() == LEADER){
//			data = receiveNewDataRequest(interest);
//			if(data!=null){
////				System.out.println("是否为新数据生成interest" + data.getName());
//				router = this.getAllRouter().get(interest.getSrcAddr());
//				this.forward(router, data);
//				Interest newDataInterest = this.createNewDataInterest(interest);
//				this.getRequestList().add(newDataInterest);
//				this.sendInterestByFib(newDataInterest);
//			}else if(interest.getName().getPrefix().contains(SYNCFLAG)){//判断是否为sync数据包
//				String interestName = interest.getName().getPrefix();
//				String digest = this.getDigestFromInterest(interestName);
//			}
			if(isNewDataRequestInterest(interest)){
				responseNewDataRequest(interest);
			}else if(isSyncInterest(interest)){
				List<String> needSyncData = digestLog.get(interest.getName().getPrefix().split(SYNCFLAG)[1]);
				Data syncResponseData = new Data(interest.getName());
				String content = "";
				for(String oneName:needSyncData){
					content+=(","+oneName);
				}
				content=content.substring(1);
				syncResponseData.setContent(content);
				this.responseDataByPit(data);
			}
		}
	}
	
	//判断是否为新数据生成interest
	public boolean isNewDataRequestInterest(Interest interest){
		String name = interest.getName().getPrefix();
		if(name.contains(NEWDATAFLAG) && name.contains(NOWDIGESTFLAG)){
			return true;
		}else{
			return false;
		}
	}
	//判断是否为SYNC interest
	public boolean isSyncInterest(Interest interest){
		String name = interest.getName().getPrefix();
		if(name.contains(SYNCFLAG)) return true;
		return false;
	}
	
	//对新数据生成Interest的回复
	public void responseNewDataRequest(Interest interest){
		String idx = interest.getName().getPrefix().split(NOWDIGESTFLAG)[1].split("/")[0];
		String dataName = interest.getName().getPrefix().split(NEWDATAFLAG)[1];
		dataName = dataName.substring(dataName.indexOf("/"));
//		System.out.println(dataName);
		String group;
		if(dataName.startsWith("/")) group=dataName.split("/")[1];
		else group = dataName.split("/")[0];
		Data data=new Data(interest.getName());;
		if(!idx.equals(digestLog.length(dataName).toString())){
//			System.out.println("idx!=lenth"+idx +" "+ digestLog.length(dataName));
			data.setContent("refuse");
		}else{
			if(lockMap.containsKey(group)){
				WRLock lock = lockMap.get(group);
				if(lock.status()!=0){
					data.setContent("refuse");
				}else{
					data.setContent("agree");
					lock.lockW();
				}
			}else{
				lockMap.put(group,new WRLockImp());
				lockMap.get(group).lockW();
				data.setContent("agree");
			}
		}
		System.out.print("data de content is: "+data.getContent());
		responseDataByPit(data);
		if(data.getContent()=="agree"){
			Interest newDataInterest = this.createNewDataInterest(interest);
			this.getRequestList().add(newDataInterest);
			this.sendInterestByFib(newDataInterest);
		}
	}
	
	public String getDigestFromInterest(String name){
		int n = name.indexOf(SYNCFLAG);
		String str = name.substring(n+SYNCFLAG.length());
		return str.split("/")[0];
	}
	
	public Data getDataWithCs(Name name){
		Data data=null;
		String fullName = name.getPrefix();
		for(String prefix:this.getPrefixSet()){
			
			if(fullName.startsWith(prefix)){
				System.out.println("router "+this.getAddress()+" find data from cs name is:"+fullName.substring(prefix.length()));
				data = this.getCs().get(new Name(fullName.substring(prefix.length())));
			}
			if(data!=null){
				break;
			}
		}
		if(data == null){
			data = this.getCs().get(name);
		}
		return data;
	}
	
	public Set<String> getPrefixSet() {
		return prefixSet;
	}

	public void setPrefixSet(Set<String> prefixSet) {
		this.prefixSet = prefixSet;
	}

	public Name generateNewName(Name name){
		Cs cs = this.getCs();
		if(cs.hasData(name)){
			String[] prefix = name.getPrefix().split("/");
			String num = prefix[prefix.length-1];
			num = String.valueOf(Integer.valueOf(num)+1);
			String newNamePrefix="";
			for(int i = 0;i<prefix.length-1;i++){
				newNamePrefix=newNamePrefix+prefix[i]+"/";
			}
			newNamePrefix+=num;
			Name newName = new Name(newNamePrefix); 
			return newName;
		}else{
			String prefix = name.getPrefix();
			prefix=prefix+"/"+"0";
			Name newName = new Name(prefix);
			return newName;
		}
	}
	
	public Data generateNewData(Name name){
		Name newName = this.generateNewName(name);
		Data newData = new Data(newName);
		this.tempData = newData;
		return newData;
	}
	
	public void newDataRequest(Name name){
		Name newName = this.generateNewName(name);
		Interest interest = new Interest(name);
	}
	
	public Interest createNewDataInterest(Interest interest){
		String prefix = interest.getName().getPrefix();
		prefix = "/"+prefix.split(NEWDATAFLAG)[1];
		Interest newDataInterest = new Interest(prefix);
		return newDataInterest;
	}
	
	public Data receiveNewDataRequest(Interest interest){
		if(interest.getInterestType()==Interest.NEWDATAREQUEST){
			return this.createNewDataResponse(interest);
		}
		return null;
	}
	
	public Data createNewDataResponse(Interest interest){
		Data data = new Data(interest.getName());
//		if(LockMap.containsKey(key))
		data.setContent("agree");
		return data;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Name name = new Name("/test1");
		int flag = 1;
		while(flag==1){
			try {
				Thread.sleep(2000);
				Iterator<Interest> it = this.getRequestList().iterator();
				while(it.hasNext()){
					Interest interest = it.next();
					Iterator<Router> rit = this.getNextRouterList().iterator();
					while(rit.hasNext()){
						this.forward(rit.next(), interest);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(this.tempData==null){
				Cs cs = this.getCs();
				Data data = cs.getLastDataWithPrefix(name);
				System.out.print("cs last data is"+ data.getName().getPrefix());
				Name newDataName = this.generateNewName(data.getName());
				this.generateNewData(data.getName());
				this.getCs().add(tempData);
				Interest newDataInterest = new Interest("/leader" + 
						NOWDIGESTFLAG + currentDigest + 
						"/newData"+this.getUniqName()+newDataName.getPrefix());
				newDataInterest.setInterestType(Interest.NEWDATAREQUEST);
				this.getRequestList().add(newDataInterest);
			}
			if(this.getRequestList().isEmpty()){
				flag=0;
			}
		}
	}
	@Override
	public synchronized Interest match(Data data) throws CloneNotSupportedException {
		Interest interest = super.match(data);
		if(interest==null){
			System.out.println("consumer "+this.getAddress()+" not match data "+data.getName());
			return null;
		}
		System.out.println(interest.getName() + " "+interest.getInterestType());
		if(interest!=null && interest.getInterestType()==Interest.NEWDATAREQUEST){
			//收到新数据生成请求回复data
			if(data.getContent() == "agree"){
				this.getCs().add(this.tempData);
				System.out.println("router "+this.getAddress()+" cs add data " + this.tempData.getName());
				this.digestLog.add(this.tempData.getName().getPrefix());
				currentDigest = this.digestLog.length(tempData.getName().getPrefix()).toString();
				System.out.println("router "+ this.getAddress() + 
						" generate Digest "+currentDigest + 
						" with data "+tempData.getName());
				handlePit(this.tempData);
				this.tempData = null;
			}else{
				sendSYNC();
				this.tempData=null;
			}
		}else if(receiveSYNCResponse(data)){
			String content = data.getContent();
			for(String name:content.split(",")){
				Interest inst = new Interest("/leader/"+name);
				this.sendInterestByFib(inst);
			}
		}
		else{
			for(String uniqName:allRouterName){
				String fullName = data.getName().getPrefix();
				if(fullName.startsWith(uniqName)){
					Data storeData = (Data)data.clone();
					storeData.setName(new Name(fullName.substring(uniqName.length())));
					this.getCs().add(storeData);
					if(this.getsConsumerType()==LEADER){
						this.lockMap.get(storeData.getName().getPrefix().split("/")[1]).rlsW();
					}
					this.digestLog.add(storeData.getName().getPrefix());
					currentDigest = digestLog.length(storeData.getName().getPrefix()).toString();
					System.out.println("router "+ this.getAddress() + 
							" generate Digest "+currentDigest + 
							" with data "+storeData.getName());
					System.out.println("router "+ this.getAddress()+" cs add data "+storeData.getName());
					handlePit(storeData);
					break;
				}
			}
		}
		return interest;
	}
	public boolean receiveSYNCResponse(Data data){
		if(data.getName().getPrefix().contains(SYNCFLAG)) return true;
		else return false;
	}
	
	//发送sync兴趣包用来同步数据
	public void sendSYNC(){
		String prefix = "/leader" + SYNCFLAG+currentDigest;
		Interest interest = new Interest(prefix);
		this.sendInterestByFib(interest);
	}
	//为无名字的data加上服务器专属名用来传输
	public Data dataAddPrefix(Data data){
		try {
			Data newData = (Data)data.clone();
			newData.setName(new Name(uniqName+data.getName().getPrefix()));
			return newData;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//去除data名字前的服务器专属名用来存入CS
	public Data dataRmPrefix(Data data){
		for(String uniqName:allRouterName){
			String fullName = data.getName().getPrefix();
			if(fullName.startsWith(uniqName)){
				try {
					Data newData = (Data)data.clone();
					newData.setName((new Name(fullName.substring(uniqName.length()))));
					return newData;
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return data;
	}
	
	public void handlePit(Data data){
		String fullName = uniqName + data.getName().getPrefix();
		try {
			Data forwardData = (Data)data.clone();
			forwardData.setName(new Name(fullName));
			this.responseDataByPit(forwardData);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public void addPrefix(String prefix){
		this.prefixSet.add(prefix);
	}
	
	public static void main(String[] args){
		SyncConsumer sc = new SyncConsumer();
		String test = "/aasa/adfs" + SYNCFLAG +"000000000/ASDFASD/SDFAS";
		System.out.println(sc.getDigestFromInterest(test));
	}
}
