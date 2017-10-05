package zkSync;

import com.ndnPackage.Name;

public class Message {
	public Message(){}
	public Message(Integer hashRoot, Name newName) {
		super();
		this.hashRoot = hashRoot;
		this.newName = newName;
	}
	private Integer hashRoot;
	private Name newName;
	public Integer getHashRoot() {
		return hashRoot;
	}
	public void setHashRoot(Integer hashRoot) {
		this.hashRoot = hashRoot;
	}
	public Name getNewName() {
		return newName;
	}
	public void setNewName(Name newName) {
		this.newName = newName;
	}
}
