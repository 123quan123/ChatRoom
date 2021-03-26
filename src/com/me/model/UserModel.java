package com.me.model;

public class UserModel {
	private String id;
	
	private String name;
	
	private String publicKey;
	private String privateKey;
	private String serverPuk;
	private String aesKey;
	
	public UserModel() {
	}
	
	public UserModel(String id) {
		super();
		this.id = id;
	}
	
	public UserModel(String id, String prk) {
		super();
		this.id = id;
		this.privateKey = prk;
	}

	public UserModel(String id, String aesKey, String name, String publicKey, String privateKey) {
		super();
		this.id = id;
		this.aesKey = aesKey;
		this.name = name;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public UserModel(String id, String name, String aesKey) {
		super();
		this.aesKey = aesKey;
		this.name = name;
		this.id = id;
	}
	
	public UserModel(String id, String serverPuk, String name, String publicKey, String privateKey, String aesKey) {
		super();
		this.id = id;
		this.serverPuk = serverPuk;
		this.name = name;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.aesKey = aesKey;
	}

	public UserModel(String aesKey, String name, String publicKey, String privateKey) {
		this.name = name;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.aesKey = aesKey;
	}

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

	public String getServerPuk() {
		return serverPuk;
	}

	public void setServerPuk(String serverPuk) {
		this.serverPuk = serverPuk;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aesKey == null) ? 0 : aesKey.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((privateKey == null) ? 0 : privateKey.hashCode());
		result = prime * result + ((publicKey == null) ? 0 : publicKey.hashCode());
		result = prime * result + ((serverPuk == null) ? 0 : serverPuk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserModel other = (UserModel) obj;
		if (aesKey == null) {
			if (other.aesKey != null)
				return false;
		} else if (!aesKey.equals(other.aesKey))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (privateKey == null) {
			if (other.privateKey != null)
				return false;
		} else if (!privateKey.equals(other.privateKey))
			return false;
		if (publicKey == null) {
			if (other.publicKey != null)
				return false;
		} else if (!publicKey.equals(other.publicKey))
			return false;
		if (serverPuk == null) {
			if (other.serverPuk != null)
				return false;
		} else if (!serverPuk.equals(other.serverPuk))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "账号 : " + id  + "   " + "昵称 : " + name;
	}

}
