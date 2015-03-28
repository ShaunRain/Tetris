package ScoreUDP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import model.TetrisBoard;

/**
 * @Shaun Rain
 * 2014
 */
public class ScoreUDPClient {
	public ScoreUDPClient() throws Exception{
		int n = TetrisBoard.score;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeInt(n);
		
		byte[] buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buf,buf.length,
				new InetSocketAddress("169.254.181.236",5666));
		DatagramSocket ds = new DatagramSocket(6666);
		ds.send(dp);
		ds.close();
	}
}
