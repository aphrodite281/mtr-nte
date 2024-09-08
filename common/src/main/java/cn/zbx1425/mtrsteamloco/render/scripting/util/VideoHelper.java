package cn.zbx1425.mtrsteamloco.render.scripting.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.minecraft.resources.ResourceLocation;

public class VideoHelper {

    public static void getFrame(ResourceLocation path1, int frame, ResourceLocation path2) throws IOException {
        File videoFile = new File(grp(path1));
        File outputFile = new File(grp(path2));

        if (!videoFile.exists()) {
            throw new IOException("Video file does not exist: " + videoFile.getAbsolutePath());
        }

        FFprobe ffprobe = new FFprobe("ffprobe");
        FFmpegProbeResult probeResult = ffprobe.probe(videoFile.getAbsolutePath());

        FFmpegStream videoStream = probeResult.getStreams().get(0);
        int duration = (int) videoStream.duration;

        float frameTime = (float) frame / videoStream.nb_frames * duration;

        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(videoFile.getAbsolutePath())
            .addExtraArgs("-ss", String.valueOf(frameTime))
            .addOutput(outputFile.getAbsolutePath())
            .setFrames(1)
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public static void getSound(ResourceLocation path1, ResourceLocation path2) throws IOException {
        File videoFile = new File(grp(path1));
        File outputFile = new File(grp(path2));

        if (!videoFile.exists()) {
            throw new IOException("Video file does not exist: " + videoFile.getAbsolutePath());
        }

        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(videoFile.getAbsolutePath())
            .addOutput(outputFile.getAbsolutePath())
            .setAudioCodec("libvorbis")
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();
    }

    private static String grp(ResourceLocation path){
        return "assets/" + path.getNamespace() + "/" + path.getPath();
    }
}