package ru.octol1ttle.flightassistant.serialization;

import dev.isxander.yacl3.platform.YACLPlatform;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.MinecraftVersion;
import org.jetbrains.annotations.Nullable;
import ru.octol1ttle.flightassistant.FlightAssistant;
import ru.octol1ttle.flightassistant.computers.navigation.Waypoint;
import ru.octol1ttle.flightassistant.serialization.api.ISerializableFactory;
import ru.octol1ttle.flightassistant.serialization.api.ISerializableList;
import ru.octol1ttle.flightassistant.serialization.api.ISerializableObject;
import ru.octol1ttle.flightassistant.serialization.json.JsonFactory;
import ru.octol1ttle.flightassistant.serialization.nbt.NbtFactory;

public class FlightPlanSerializer {
    private static final Path PLAN_PATH = YACLPlatform.getConfigDir().resolve("%s/plans/".formatted(FlightAssistant.MODID));

    static final ISerializableFactory NBT_FACTORY = new NbtFactory();
    static final ISerializableFactory JSON_FACTORY = new JsonFactory();
    static final ISerializableFactory WRITE_FACTORY = JSON_FACTORY;

    public static void save(List<Waypoint> plan, String name) {
        ISerializableObject planObject = WRITE_FACTORY.createObject();

        ISerializableList<ISerializableObject> list = WRITE_FACTORY.createList(plan.size());

        for (Waypoint waypoint : plan) {
            list.add(WaypointSerializer.save(waypoint, WRITE_FACTORY.createObject()));
        }

        planObject.put("Waypoints", list);

        try {
            Files.createDirectories(PLAN_PATH);
            WRITE_FACTORY.createSerializer().write(PLAN_PATH, name, planObject);
        } catch (IOException e) {
            FlightAssistant.LOGGER.error("IO error detected during flight plan serialization with name: %s".formatted(name), e);
        }
    }

    public static @Nullable List<Waypoint> load(String name) {
        List<Waypoint> loaded = new ArrayList<>();

        try {
            ISerializableObject object = JSON_FACTORY.createSerializer().read(PLAN_PATH, name);
            if (object == null && MinecraftVersion.CURRENT.getProtocolVersion() >= 765) {
                object = NBT_FACTORY.createSerializer().read(PLAN_PATH, name);
            }

            if (object == null) {
                return null;
            }
            ISerializableList<ISerializableObject> list = object.getList("Waypoints");
            for (ISerializableObject waypointObject : list) {
                loaded.add(WaypointSerializer.load(waypointObject));
            }
        } catch (IllegalStateException e) {
            FlightAssistant.LOGGER.error("Invalid data detected during flight plan deserialization with name: %s".formatted(name), e);
        } catch (IOException e) {
            FlightAssistant.LOGGER.error("IO error detected during flight plan deserialization with name: %s".formatted(name), e);
        }

        return loaded;
    }
}
