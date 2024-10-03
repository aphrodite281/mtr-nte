package cn.zbx1425.mtrsteamloco.network.util;

import mtr.data.ScheduleEntry;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Serializer {

    public static byte[] serialize(Map<String, String> map) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(map.size());

        for (Map.Entry<String, String> entry : map.entrySet()) {
            dos.writeUTF(entry.getKey());
            dos.writeUTF(entry.getValue());
        }

        dos.flush();
        return baos.toByteArray();
    }

    public static Map<String, String> deserialize(byte[] bytes) throws IOException {
        Map<String, String> map = new HashMap<>();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);

        int size = dis.readInt();

        for (int i = 0; i < size; i++) {
            String key = dis.readUTF();
            String value = dis.readUTF();
            map.put(key, value);
        }

        dis.close();
        return map;
    }

    public static byte[] serialize(List<ScheduleEntry> list) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(list.size());

        for (ScheduleEntry entry : list) {
            dos.writeLong(entry.arrivalMillis);
            dos.writeInt(entity.trainCars);
            dos.writeLong(entry.routeId);
            dos.writeInt(entry.currentStationIndex);
        }

        dos.flush();
        return baos.toByteArray();
    }

    public static List<ScheduleEntry> deserialize(byte[] bytes, boolean a) throws IOException {
        List<ScheduleEntry> list = new ArrayList<>();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);

        int size = dis.readInt();

        for (int i = 0; i < size; i++) {
            long arrivalMillis = dis.readLong();
            int trainCars = dis.readInt();
            long routeId = dis.readLong();
            int currentStationIndex = dis.readInt();
            list.add(new ScheduleEntry(arrivalMillis, trainCars, routeId, currentStationIndex));
        }

        dis.close();
        return list;
    }
}