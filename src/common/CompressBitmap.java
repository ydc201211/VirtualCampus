package common;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class CompressBitmap {
	// ͼƬѹ��
	public static Bitmap compressBitmap(Bitmap bitmap, double newWidth,
			double newHeight) {

		Bitmap beforeBitmap = bitmap;

		float beforeWidth = beforeBitmap.getWidth();
		float beforeHeight = beforeBitmap.getHeight();

		float scaleWidth = 0;
		float scaleHeight = 0;
		if (beforeWidth > beforeHeight) {
			scaleWidth = ((float) newWidth) / beforeWidth;
			scaleHeight = ((float) newHeight) / beforeHeight;
		} else {
			scaleWidth = ((float) newWidth) / beforeHeight;
			scaleHeight = ((float) newHeight) / beforeWidth;
		}

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap afterBitmap = Bitmap.createBitmap(beforeBitmap, 0, 0,
				(int) beforeWidth, (int) beforeHeight, matrix, true);
		return afterBitmap;

	}
}
