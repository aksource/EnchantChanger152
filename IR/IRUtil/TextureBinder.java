package IR.IRUtil;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * ブロックやアイテムのアイコン用画像を256個ずつ1つのファイルに纏めるためのクラス
 * @author gnsk
 *
 */
public class TextureBinder {

	public interface IImageLoader {
		BufferedImage load(String path) throws IOException;
	}

	private static final String outputFileNameFormat = "_irtemptex%d_.png";
	private static int textureFileIndex = 0;

	/**
	 * 各レコード用の画像を256個ずつ1つのファイルに纏め、textureFileとtextrueIndexを再割当てする
	 * @param homeDir 作業ディレクトリ
	 * @param records 対象のレコード canBindTextureがTrueの場合のみ処理する
	 * @param loader 画像を読み込む方法を指定する
	 * @return
	 * @throws IOException
	 */
	public static String[] bindTextures(File homeDir, Record[] records, IImageLoader loader) throws IOException {
		List<String> textureList = collectBindingTexture(records);
		return makeBindedTextures(homeDir, textureList, loader);
	}

	private static List<String> collectBindingTexture(Record[] records) {
		List<String> textureList = new ArrayList<String>();

		for (Record record : records) {
			if (record.canBindTexture) {

				int i = textureList.indexOf(record.textureFile);
				if (i == -1) {
					i = textureList.size();
					textureList.add(record.textureFile);
				}

				record.textrueIndex = i % 256;
				record.textureFile = getResourcePath(i / 256);
			}
		}

		return textureList;
	}

	private static String[] makeBindedTextures(File homedir, List<String> textureList, IImageLoader loader) throws IOException {
		BufferedImage workingImage = null;
		Graphics2D g = null;
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < textureList.size(); i++) {
			int j = i % 256;
			if (j == 0) {
				if (workingImage == null) {
					workingImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
					g = workingImage.createGraphics();
				} else {
					list.add(saveImage(workingImage, (i - 1) / 256, homedir));
					g.clearRect(0, 0, 256, 256);
				}
			}

			int x = j % 16;
			int y = j / 16;

			String t = textureList.get(i);
			BufferedImage icon;
			icon = loader.load(t);
			g.drawImage(icon, x * 16, y * 16, 16, 16, null);
		}

		if (workingImage != null) {
			list.add(saveImage(workingImage, (textureList.size() - 1) / 256, homedir));
			g.dispose();
		}

		textureFileIndex += list.size();
		return list.toArray(new String[list.size()]);
	}

	private static File getTextureFilePath(int index, File homedir) {
		return new File(homedir, String.format(outputFileNameFormat, index + textureFileIndex));
	}

	private static String getResourcePath(int index) {
		return "/" + String.format(outputFileNameFormat, index + textureFileIndex);
	}

	private static String saveImage(BufferedImage img, int index, File homedir) throws IOException {
		File path = getTextureFilePath(index, homedir);
		ImageIO.write(img, "PNG", path);
		path.deleteOnExit();
		return getResourcePath(index);
	}
}
