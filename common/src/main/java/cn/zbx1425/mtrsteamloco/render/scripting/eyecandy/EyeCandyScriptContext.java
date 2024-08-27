package cn.zbx1425.mtrsteamloco.render.scripting.eyecandy;

import cn.zbx1425.mtrsteamloco.block.BlockEyeCandy;
import cn.zbx1425.mtrsteamloco.render.scripting.AbstractScriptContext;
import cn.zbx1425.mtrsteamloco.render.scripting.util.DynamicModelHolder;
import cn.zbx1425.sowcer.math.Matrices;
import cn.zbx1425.sowcer.math.Matrix4f;
import cn.zbx1425.sowcerext.model.ModelCluster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import cn.zbx1425.mtrsteamloco.sound;

public class EyeCandyScriptContext extends AbstractScriptContext {

    public BlockEyeCandy.BlockEntityEyeCandy entity;

    public EyeCandyDrawCalls scriptResult;
    private EyeCandyDrawCalls scriptResultWriting;

    public EyeCandyScriptContext(BlockEyeCandy.BlockEntityEyeCandy entity) {
        scriptResult = new EyeCandyDrawCalls();
        scriptResultWriting = new EyeCandyDrawCalls();
        this.entity = entity;
    }

    @Override
    public void renderFunctionFinished() {
        synchronized (this) {
            EyeCandyDrawCalls temp = scriptResultWriting;
            scriptResultWriting = scriptResult;
            scriptResult = temp;
            scriptResultWriting.reset();
        }
    }

    @Override
    public Object getWrapperObject() {
        return entity;
    }

    // Something more graceful?
    public boolean disposeForReload = false;

    @Override
    public boolean isBearerAlive() {
        return !disposeForReload && !entity.isRemoved();
    }

    public void drawModel(ModelCluster model, Matrices poseStack) {
        scriptResultWriting.addModel(model, poseStack == null ? Matrix4f.IDENTITY : poseStack.last().copy());
    }

    public void drawModel(DynamicModelHolder model, Matrices poseStack) {
        scriptResultWriting.addModel(model, poseStack == null ? Matrix4f.IDENTITY : poseStack.last().copy());
    }

    public void playSound(ResourceLocation sound, float volume, float pitch) {
        scriptResultWriting.addSound(
#if MC_VERSION >= "11903"
                SoundEvent.createVariableRangeEvent(sound),
#else
                new SoundEvent(sound),
#endif
                volume, pitch
        );
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        scriptResultWriting.addSound(sound, volume, pitch);
    }

    public SoundEvent createSoundEvent(ResourceLocation sound) {
#if MC_VERSION >= "11903"
        return SoundEvent.createVariableRangeEvent(sound);
#else
        return new SoundEvent(sound);
    }

    public void stopAllSounds() {
        
    }

    public TickableSound createTickableSound(SoundEvent sound){
        return new TickableSound(sound);
    }

    public void playTickableSound(TickableSound sound) {
        scriptResultWriting.addTickableSound(sound);
        //Minecraft.getInstance().getSoundManager().play(sound);
    }
}
