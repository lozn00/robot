package lozn.wol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WakeOnLan {

    public static final int PORT = 7778;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java WakeOnLan <broadcast-ip> <mac-address>");
            System.out.println("Example: java WakeOnLan 192.168.0.255 00:0D:61:08:22:4A");
            System.out.println("Example: java WakeOnLan 192.168.0.255 00-0D-61-08-22-4A");
            System.exit(1);
        }

        String ipStr = args[0];
        String macStr = args[1];
        try {
            awake(ipStr, macStr);
        } catch (Exception e) {
            System.out.println("Failed to send Wake-on-LAN packet: "+e.toString());
            System.exit(1);
        }

    }

    public static void awake(String ipStr, String macStr) throws IOException {
        byte[] macBytes = getMacBytes(macStr);
        byte[] bytes = new byte[6 + 16 * macBytes.length];
        //写入连续6个FF
        for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) 0xff;
        }
        //写入mac地址16次
        for (int i = 6; i < bytes.length; i += macBytes.length) {
            System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
        }

        InetAddress address = InetAddress.getByName(ipStr);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        socket.close();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(String.valueOf(Integer.valueOf(bytes[i])));
        }
        System.out.println("Wake-on-LAN packet sent:  " + sb);

    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

}
