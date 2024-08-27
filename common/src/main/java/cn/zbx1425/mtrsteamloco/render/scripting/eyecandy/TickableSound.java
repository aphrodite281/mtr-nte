package cn.zbx1425.mtrsteamloco.sound;

import mtr.mappings.TickableSoundInstanceMapper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TickableSound extends TickableSoundInstanceMapper{
    public TickableSound(SoundEvent event){
        super(event, SoundSource.BLOCKS);
    }

    public void setVolume(float volume){
        this.volume = volume;
    }

    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setZ(double z){
        this.z = z;
    }

    public void setLooping(boolean looping){
        this.looping = looping;
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void setRelative(boolean relative){
        this.relative = relative;
    }

    public void stop(){
        stop();
    }
}
