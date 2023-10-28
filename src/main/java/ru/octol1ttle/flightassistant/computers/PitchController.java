package ru.octol1ttle.flightassistant.computers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import ru.octol1ttle.flightassistant.FlightAssistant;
import ru.octol1ttle.flightassistant.indicators.PitchIndicator;

public class PitchController {
    public static final float CLIMB_PITCH = -55.0f;
    private final FlightComputer computer;
    /**
     * USE MINECRAFT PITCH (minus is up and plus is down)
     **/
    public Float targetPitch = null;
    public boolean forceLevelOff = false;
    public boolean forceClimb = false;

    public PitchController(FlightComputer computer) {
        this.computer = computer;
    }

    public void tick(float delta) {
        if (computer.pitch > computer.stall.maximumSafePitch) {
            smoothSetPitch(-computer.stall.maximumSafePitch, delta);
            return;
        }
        if (computer.pitch < computer.voidDamage.minimumSafePitch) {
            smoothSetPitch(-computer.voidDamage.minimumSafePitch, delta);
            return;
        }
        if (forceLevelOff) {
            smoothSetPitch(0.0f, MathHelper.clamp(delta / computer.gpws.descentImpactTime, 0.001f, 1.0f));
            return;
        }
        if (forceClimb) {
            smoothSetPitch(CLIMB_PITCH, MathHelper.clamp(delta / computer.gpws.terrainImpactTime, 0.001f, 1.0f));
            return;
        }

        smoothSetPitch(targetPitch, delta);
    }

    /**
     * Smoothly changes the player's pitch to the specified pitch using the delta
     *
     * @param pitch Target MINECRAFT pitch (- is up, + is down)
     * @param delta Delta time, in seconds
     */
    public void smoothSetPitch(Float pitch, float delta) {
        if (pitch == null) {
            return;
        }
        checkFloatValidity(pitch, "Target pitch");

        PlayerEntity player = computer.player;
        float difference = pitch - player.getPitch();

        if (difference < 0) { // going UP
            pitch = MathHelper.clamp(pitch, -computer.stall.maximumSafePitch, 90.0f);
        }
        if (difference > 0) { // going DOWN
            pitch = MathHelper.clamp(pitch, -90.0f, -PitchIndicator.DANGEROUS_DOWN_PITCH);
        }

        float newPitch = player.getPitch() + (pitch - player.getPitch()) * delta;
        checkFloatValidity(newPitch, "New pitch",
                new Pair<>("Current pitch", player.getPitch()),
                new Pair<>("Target pitch", pitch),
                new Pair<>("Delta", delta));

        player.setPitch(newPitch);
    }

    @SafeVarargs
    private void checkFloatValidity(Float f, String name, Pair<String, Float>... additionalInfo) {
        if (f.isNaN() || f.isInfinite() || f < -90.0f || f > 90.0f) {
            FlightAssistant.LOGGER.error("{} out of bounds: {}", name, f);
            FlightAssistant.LOGGER.error("Additional information:");
            for (Pair<String, Float> pair : additionalInfo) {
                FlightAssistant.LOGGER.error("{}: {}", pair.getLeft(), pair.getRight());
            }
            throw new IllegalArgumentException();
        }
    }
}
