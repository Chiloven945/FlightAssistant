package ru.octol1ttle.flightassistant;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.LabelOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import java.awt.Color;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import ru.octol1ttle.flightassistant.config.FAConfig;
import ru.octol1ttle.flightassistant.config.HudConfig;

public class FAModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> YetAnotherConfigLib.create(FAConfig.HANDLER, (defaults, config, builder) -> builder
                .title(Text.translatable("mod.flightassistant"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.flightassistant.category.hud_settings"))
                        .option(Option.<FAConfig.BatchedRendering>createBuilder()
                                .name(Text.translatable("config.flightassistant.settings.batching"))
                                .available(FlightAssistant.canUseBatching())
                                .binding(FAConfig.BatchedRendering.SINGLE_BATCH, () -> config.batchedRendering, o -> config.batchedRendering = o)
                                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(FAConfig.BatchedRendering.class))
                                .build()
                        )
                        .option(Option.<Float>createBuilder()
                                .name(Text.translatable("config.flightassistant.settings.hud_scale"))
                                .binding(1.0f, () -> config.hudScale, o -> config.hudScale = o)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.1f, 5.0f)
                                        .step(0.05f)
                                        .formatValue(value -> Text.literal(MathHelper.floor(value * 100.0f) + "%"))
                                )
                                .build()
                        )
                        .option(Option.<Float>createBuilder()
                                .name(Text.translatable("config.flightassistant.settings.frame_width"))
                                .binding(0.6f, () -> config.frameWidth, o -> config.frameWidth = o)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.3f, 0.9f)
                                        .step(0.05f)
                                        .formatValue(value -> Text.literal(MathHelper.floor(value * 100.0f) + "%"))
                                )
                                .build()
                        )
                        .option(Option.<Float>createBuilder()
                                .name(Text.translatable("config.flightassistant.settings.frame_height"))
                                .binding(0.6f, () -> config.frameHeight, o -> config.frameHeight = o)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .range(0.3f, 0.9f)
                                        .step(0.05f)
                                        .formatValue(value -> Text.literal(MathHelper.floor(value * 100.0f) + "%"))
                                )
                                .build()
                        )
                        .build()
                )
                .category(hud(Text.translatable("config.flightassistant.category.not_flying_no_elytra"), config.notFlyingNoElytra, new HudConfig().disableAll()))
                .category(hud(Text.translatable("config.flightassistant.category.not_flying_has_elytra"), config.notFlyingHasElytra, new HudConfig().setMinimal()))
                .category(hud(Text.translatable("config.flightassistant.category.flying"), config.flying, new HudConfig()))
        ).generateScreen(parent);
    }

    private ConfigCategory hud(Text name, HudConfig config, HudConfig defaults) {
        return ConfigCategory.createBuilder()
                .name(name)

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.color")))
                .option(Option.<Color>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.color.frame"))
                        .binding(defaults.frameColor, () -> config.frameColor, o -> config.frameColor = o)
                        .controller(ColorControllerBuilder::create)
                        .build())
                .option(Option.<Color>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.color.status"))
                        .binding(defaults.statusColor, () -> config.statusColor, o -> config.statusColor = o)
                        .controller(ColorControllerBuilder::create)
                        .build())
                .option(Option.<Color>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.color.advisory"))
                        .binding(defaults.advisoryColor, () -> config.advisoryColor, o -> config.advisoryColor = o)
                        .controller(ColorControllerBuilder::create)
                        .build())
                .option(Option.<Color>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.color.caution"))
                        .binding(defaults.cautionColor, () -> config.cautionColor, o -> config.cautionColor = o)
                        .controller(ColorControllerBuilder::create)
                        .build())
                .option(Option.<Color>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.color.warning"))
                        .binding(defaults.warningColor, () -> config.warningColor, o -> config.warningColor = o)
                        .controller(ColorControllerBuilder::create)
                        .build())

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.speed")))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.speed.scale"))
                        .binding(defaults.showSpeedScale, () -> config.showSpeedScale, o -> config.showSpeedScale = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.speed.readout"))
                        .binding(defaults.showSpeedReadout, () -> config.showSpeedReadout, o -> config.showSpeedReadout = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.speed.ground_readout"))
                        .binding(defaults.showGroundSpeedReadout, () -> config.showGroundSpeedReadout, o -> config.showGroundSpeedReadout = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.speed.vertical_readout"))
                        .binding(defaults.showVerticalSpeedReadout, () -> config.showVerticalSpeedReadout, o -> config.showVerticalSpeedReadout = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.altitude")))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.altitude.scale"))
                        .binding(defaults.showAltitudeScale, () -> config.showAltitudeScale, o -> config.showAltitudeScale = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.altitude.readout"))
                        .binding(defaults.showAltitudeReadout, () -> config.showAltitudeReadout, o -> config.showAltitudeReadout = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.altitude.ground"))
                        .binding(defaults.showGroundAltitude, () -> config.showGroundAltitude, o -> config.showGroundAltitude = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.heading")))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.heading.scale"))
                        .binding(defaults.showHeadingScale, () -> config.showHeadingScale, o -> config.showHeadingScale = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.heading.readout"))
                        .binding(defaults.showHeadingReadout, () -> config.showHeadingReadout, o -> config.showHeadingReadout = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.automation")))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.automation.firework"))
                        .binding(defaults.showFireworkMode, () -> config.showFireworkMode, o -> config.showFireworkMode = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.automation.vertical"))
                        .binding(defaults.showVerticalMode, () -> config.showVerticalMode, o -> config.showVerticalMode = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.automation.lateral"))
                        .binding(defaults.showLateralMode, () -> config.showLateralMode, o -> config.showLateralMode = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.automation.status"))
                        .binding(defaults.showAutomationStatus, () -> config.showAutomationStatus, o -> config.showAutomationStatus = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.info")))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.info.alerts"))
                        .binding(defaults.showAlerts, () -> config.showAlerts, o -> config.showAlerts = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.info.firework_count"))
                        .binding(defaults.showFireworkCount, () -> config.showFireworkCount, o -> config.showFireworkCount = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())

                .option(LabelOption.create(Text.translatable("config.flightassistant.hud.misc")))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.misc.pitch_ladder"))
                        .binding(defaults.showPitchLadder, () -> config.showPitchLadder, o -> config.showPitchLadder = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.misc.flight_path"))
                        .binding(defaults.showFlightPath, () -> config.showFlightPath, o -> config.showFlightPath = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.misc.coordinates"))
                        .binding(defaults.showCoordinates, () -> config.showCoordinates, o -> config.showCoordinates = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("config.flightassistant.hud.misc.elytra_health"))
                        .binding(defaults.showElytraHealth, () -> config.showElytraHealth, o -> config.showElytraHealth = o)
                        .controller(TickBoxControllerBuilder::create)
                        .build())

                .build();
    }
}
