package com.me.CSFramework.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Communication implements Runnable {
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private volatile boolean goon;
	private Object lock;
	
	Communication(Socket socket) {
		try {
			this.lock = new Object();
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			synchronized (lock) {
				this.goon = true;
				new Thread(this, "COMMUNICATION").start();
				// 如果这里也存在代码，则，它们一定在子线程真正运行之前执行。
				try {
					lock.wait();
					// wait()方法，在阻塞自身线程前，必然开lock锁；
					// 当该线程被其它线程notify()后，将再次进入lock锁块，而进一步对lock上锁。
				} catch (InterruptedException e) {
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract void peerAbnormalDrop();
	protected abstract void dealNetMessage(NetMessage message);
	
	@Override
	public void run() {
		String message = null;
		synchronized (lock) {
			lock.notify();
		}
		
		while (goon) {
			try {
				message = dis.readUTF();
				dealNetMessage(new NetMessage(message));
			} catch (IOException e) {
				if (goon == true) {
					goon = false;
					peerAbnormalDrop();
				}
//				e.printStackTrace();
			}
		}
		close();
	}

	void send(NetMessage netMessage) {
		try {
			dos.writeUTF(netMessage.toString());
		} catch (IOException e) {
			close();
			e.printStackTrace();
		}
	}
	
	void close() {
		this.goon = false;
		try {
			if (this.dis != null) {
				this.dis.close();
			}
		} catch (IOException e) {
		} finally {
			this.dis = null;
		}
		try {
			if (this.dos != null) {
				this.dos.close();
			}
		} catch (IOException e) {
		} finally {
			this.dos = null;
		}
		try {
			if (this.socket != null && !this.socket.isClosed()) {
				this.socket.close();
			}
		} catch (IOException e) {
		} finally {
			this.socket = null;
		}
	}
}
