package cn.zbx1425.mtrsteamloco.forge;

import cn.zbx1425.mtrsteamloco.Main;
import cn.zbx1425.mtrsteamloco.MainClient;
import cn.zbx1425.mtrsteamloco.gui.ConfigScreen;
import cn.zbx1425.mtrsteamloco.render.train.SteamSmokeParticle;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
#if MC_VERSION >= "11900"
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
#else
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
#endif
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientProxy {

    public static void initClient() {

    }


    public static class ModEventBusListener {

        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            MainClient.init();
        }

        @SubscribeEvent
#if MC_VERSION >= "11900"
        public static void onRegistryParticleFactory(RegisterParticleProvidersEvent event) {
#else
        public static void onRegistryParticleFactory(ParticleFactoryRegisterEvent event) {
#endif
            Minecraft.getInstance().particleEngine.register(Main.PARTICLE_STEAM_SMOKE, SteamSmokeParticle.Provider::new);
        }
    }

    public static class ForgeEventBusListener {

        @SubscribeEvent
#if MC_VERSION >= "11900"
        public static void onDebugOverlay(CustomizeGuiOverlayEvent.DebugText event) {
#else
        public static void onDebugOverlay(RenderGameOverlayEvent.Text event) {
#endif
            if (Minecraft.getInstance().options.renderDebug) {
                event.getLeft().add(
                        "[NTE] Draw Calls: " + MainClient.batchManager.drawCallCount
                                + ", Batches: " + MainClient.batchManager.batchCount
                );
            }
        }

        @SubscribeEvent
        public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
            event.getDispatcher().register(
                    Commands.literal("mtrnte")
                            .then(Commands.literal("config")
                                    .executes(context -> {
                                        Minecraft.getInstance().tell(() -> {
                                            Minecraft.getInstance().setScreen(new ConfigScreen(Minecraft.getInstance().screen));
                                        });
                                        return 1;
                                    }))
                            .then(Commands.literal("stat")
                                    .executes(context -> {
                                        Minecraft.getInstance().tell(() -> {
                                            String info = "=== NTE Rendering Status ===\n"
                                                    + "Draw Calls: " + MainClient.batchManager.drawCallCount
                                                    + ", Batches: " + MainClient.batchManager.batchCount
                                                    + ", Faces: " + MainClient.batchManager.faceCount
                                                    + "\n"
                                                    + "Loaded Models: " + MainClient.modelManager.loadedRawModels.size()
                                                    + ", Uploaded VAOs: " + MainClient.modelManager.uploadedVertArraysCount
                                                    ;
#if MC_VERSION >= "11900"
                                            Minecraft.getInstance().player.sendSystemMessage(Text.literal(info));
#else
											Minecraft.getInstance().player.sendMessage(Text.literal(info), Util.NIL_UUID);
#endif
                                        });
                                        return 1;
                                    }))
            );
        }
    }
}