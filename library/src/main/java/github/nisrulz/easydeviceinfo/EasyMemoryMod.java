/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.nisrulz.easydeviceinfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import static android.os.Build.VERSION;
import static android.os.Build.VERSION_CODES;

/**
 * The type Easy memory mod.
 *
 * Deprecation warning suppressed since it is handled in the code
 */
@SuppressWarnings("deprecation") public class EasyMemoryMod {
  private final Context context;

  /**
   * Instantiates a new Easy memory mod.
   *
   * @param context the context
   */
  public EasyMemoryMod(final Context context) {
    this.context = context;
  }

  /**
   * Gets total ram.
   *
   * @return the total ram
   */
  public final long getTotalRAM() {
    long totalMemory = 0;
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
      MemoryInfo mi = new MemoryInfo();
      ActivityManager activityManager =
          (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
      activityManager.getMemoryInfo(mi);
      totalMemory = mi.totalMem;
    } else {
      RandomAccessFile reader;
      String load;
      try {
        reader = new RandomAccessFile("/proc/meminfo", "r");
        load = reader.readLine().replaceAll("\\D+", "");
        totalMemory = (long) Integer.parseInt(load);
        reader.close();
      } catch (IOException e) {
        Log.d(EasyDeviceInfo.name, "IO Exception", e);
      }
    }
    return totalMemory;
  }

  private boolean externalMemoryAvailable() {
    return android.os.Environment.getExternalStorageState()
        .equals(android.os.Environment.MEDIA_MOUNTED);
  }

  /**
   * Gets available internal memory size.
   *
   * @return the available internal memory size
   */
  public final long getAvailableInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize;
    long availableBlocks;
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
      blockSize = stat.getBlockSizeLong();
      availableBlocks = stat.getAvailableBlocksLong();
    } else {
      blockSize = stat.getBlockSize();
      availableBlocks = stat.getAvailableBlocks();
    }
    return availableBlocks * blockSize;
  }

  /**
   * Gets total internal memory size.
   *
   * @return the total internal memory size
   */
  public final long getTotalInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize;
    long totalBlocks;
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
      blockSize = stat.getBlockSizeLong();
      totalBlocks = stat.getBlockCountLong();
    } else {
      blockSize = stat.getBlockSize();
      totalBlocks = stat.getBlockCount();
    }
    return totalBlocks * blockSize;
  }

  /**
   * Gets available external memory size.
   *
   * @return the available external memory size
   */
  public final long getAvailableExternalMemorySize() {
    if (externalMemoryAvailable()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs stat = new StatFs(path.getPath());
      long blockSize;
      long availableBlocks;
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = stat.getBlockSizeLong();
        availableBlocks = stat.getAvailableBlocksLong();
      } else {
        blockSize = stat.getBlockSize();
        availableBlocks = stat.getAvailableBlocks();
      }
      return availableBlocks * blockSize;
    } else {
      return 0;
    }
  }

  /**
   * Gets total external memory size.
   *
   * @return the total external memory size
   */
  public final long getTotalExternalMemorySize() {
    if (externalMemoryAvailable()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs stat = new StatFs(path.getPath());
      long blockSize;
      long totalBlocks;
      if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = stat.getBlockSizeLong();
        totalBlocks = stat.getBlockCountLong();
      } else {
        blockSize = stat.getBlockSize();
        totalBlocks = stat.getBlockCount();
      }
      return totalBlocks * blockSize;
    } else {
      return 0;
    }
  }

  /**
   * Convert to kb float.
   *
   * @param valInBytes the val in bytes
   * @return the float
   */
  public float convertToKb(long valInBytes) {
    return (float) valInBytes / 1024;
  }

  /**
   * Convert to mb float.
   *
   * @param valInBytes the val in bytes
   * @return the float
   */
  public float convertToMb(long valInBytes) {
    return (float) valInBytes / (1024 * 1024);
  }

  /**
   * Convert to gb float.
   *
   * @param valInBytes the val in bytes
   * @return the float
   */
  public float convertToGb(long valInBytes) {
    return (float) valInBytes / (1024 * 1024 * 1024);
  }

  /**
   * Convert to tb float.
   *
   * @param valInBytes the val in bytes
   * @return the float
   */
  public float convertToTb(long valInBytes) {
    return (float) valInBytes / (1024 * 1024 * 1024 * 1024);
  }
}
