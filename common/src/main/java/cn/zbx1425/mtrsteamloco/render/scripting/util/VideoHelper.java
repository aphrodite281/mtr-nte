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
        File videoFile = new File(path1.getPath());
        File outputFile = new File(path2.getPath());

        // 检查视频文件是否存在
        if (!videoFile.exists()) {
            throw new IOException("Video file does not exist: " + videoFile.getAbsolutePath());
        }

        // 使用 FFprobe 获取视频信息
        FFprobe ffprobe = new FFprobe("ffprobe");
        FFmpegProbeResult probeResult = ffprobe.probe(videoFile.getAbsolutePath());

        // 获取视频流信息
        FFmpegStream videoStream = probeResult.getStreams().get(0);
        int duration = (int) videoStream.duration;

        // 计算帧时间
        float frameTime = (float) frame / videoStream.nb_frames * duration;

        // 使用 FFmpeg 提取帧
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
        File videoFile = new File(path1.getPath());
        File outputFile = new File(path2.getPath());

        // 检查视频文件是否存在
        if (!videoFile.exists()) {
            throw new IOException("Video file does not exist: " + videoFile.getAbsolutePath());
        }

        // 使用 FFmpeg 提取音频并保存为 OGG 格式
        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(videoFile.getAbsolutePath())
            .addOutput(outputFile.getAbsolutePath())
            .setAudioCodec("libvorbis") // 设置音频编码器为 libvorbis，用于生成 OGG 格式
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();
    }
}