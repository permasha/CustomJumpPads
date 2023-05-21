package ru.permasha.customjumppads.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class JsonSerializer {

    public static Location fromJsonLocation(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject object = jsonParser.parse(json).getAsJsonObject();

        String worldName = object.get("world").getAsString();

        JsonArray to = object.getAsJsonArray("location");
        int x = to.get(0).getAsInt();
        int y = to.get(1).getAsInt();
        int z = to.get(2).getAsInt();

        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    public static String toJsonLocation(Location location) {
        JsonObject object = new JsonObject();

        object.addProperty("world", location.getWorld().getName());

        JsonArray loc = new JsonArray();
        loc.add(location.getBlockX());
        loc.add(location.getBlockY());
        loc.add(location.getBlockZ());
        object.add("location", loc);

        return object.toString();
    }

}
