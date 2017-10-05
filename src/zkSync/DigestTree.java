package zkSync;

import java.util.HashMap;
import java.util.Map;

public class DigestTree {
	private Integer hashRoot;
	public Map<Integer,Integer> hashTree;
	
	
	public void refresh(Integer rAddress,Integer nodeHash){
		this.hashTree.put(rAddress, nodeHash);
	}
	public DigestTree(){
		this.hashRoot = new Integer(Integer.MAX_VALUE);
		this.hashTree = new HashMap<Integer,Integer>();
	}
	
	public DigestTree(Integer hashRoot, Map<Integer, Integer> hashTree) {
		super();
		this.hashRoot = hashRoot;
		this.hashTree = hashTree;
	}
	
	public DigestTree(DigestTree digestTree){
		this.hashRoot = new Integer(digestTree.getHashRoot());
		this.hashTree = new HashMap<Integer,Integer>(digestTree.getHashTree());
	}
	
	
	public void initRoot(){
		this.hashRoot = hashTree.hashCode();
	}
	public Integer getHashRoot() {
		return hashRoot;
	}
	public void setHashRoot(Integer hashRoot) {
		this.hashRoot = hashRoot;
	}
	public Map<Integer, Integer> getHashTree() {
		return hashTree;
	}
	public void setHashTree(Map<Integer, Integer> hashTree) {
		this.hashTree = hashTree;
	}
}
