## mlkit-example


## Table of Contents

 * [Introduction](#introduction)
 * [Preparation](#preparation)
 * [Installation](#installation)
 * [Experience Different Functions](#function-use)
 * [Supported Environments](#supported-environments)
 * [License](#license)


## Introduction
    The sample code describes how to use the HMS Core ML SDK, including face detection, text recognition,
    document recognition, card recognition (bank card recognition, ID card recognition, and general card recognition),
    image classification, landmark recognition, object detection and tracking, text translation,
    language detection, product visual search, image segmentation, automatic speech recognition (ASR), and text to speech (TTS).

    Main APIs in sample code:
    1. Face Detection
    MLAnalyzerFactory.getInstance().getFaceAnalyzer(): creates a face analyzer.
    MLFaceAnalyzer.setTransactor(): sets a face detection result processor for subsequent processing of the result.
    MLFaceAnalyzerSetting.Factory().setFeatureType(MLFaceAnalyzerSetting.TYPE_FEATURES): enables facial expression and feature detection, including smiling, the possibility of opening the eyes, possibility of wearing a beard, and age.
    MLFaceAnalyzerSetting.Factory().allowTracing(): indicates whether to enable the face tracking mode.

    2. Text Recognition
    MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(): creates an on-device text analyzer.
    MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer(): creates an on-cloud text analyzer.
    MLAnalyzerFactory.getInstance().getRemoteDocumentAnalyzer(): creates an on-cloud document analyzer.
    MLTextAnalyzer.asyncAnalyseFrame(MLFrame frame): recognizes text in images.
    MLDocumentAnalyzer.asyncAnalyseFrame(MLFrame frame): recognizes document information in images.
    MLText.getBlocks(): obtains text blocks. Generally, a text block corresponds to a piece of text.
    MLText.Block.getContents(): obtains a list of text lines (MLText.TextLine).
    MLText.TextLine.getContents(): obtains the text content (MLText.Word) of each line.
    MLText.Word.getStringValue(): obtains words in each line.
    MLDocument.getBlocks(): obtains document blocks. Generally, a document block (MLDocument.Block) represents multiple sections.
    MLDocument.getSections(): obtains a list of document sections (MLDocument.Section).
    MLDocument.getLineList(): obtains a list of document lines (MLDocument.Line).
    MLDocument.getWordList(): obtains a list of document words (MLDocument.Word).

    3. Image Classification
    MLAnalyzerFactory.getInstance().getLocalImageClassificationAnalyzer(): creates an on-device image classification analyzer.
    MLAnalyzerFactory.getInstance().getRemoteImageClassificationAnalyzer(): creates an on-cloud image classification analyzer.
    MLImageClassificationAnalyzer.asyncAnalyseFrame(MLFrame frame): classifies images and generates an MLImageClassification set, which contains image category information.
    MLImageClassification.getName(): obtains image category names. Example: pen, phone, computer

    4. Object Detection and Tracking
    MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(): creates an object analyzer.
    MLObjectAnalyzerSetting.Factory.setAnalyzerType(MLObjectAnalyzerSetting.TYPE_VIDEO): sets the detection mode.
    MLOject.getTypePossibility: obtains the name of an object.
    MLOject.getTypeIdentity(): obtains the tracking ID of an object.
    LensEngine: camera source that used for generating continuous image data for detection.

    5. Landmark Recognition
    MLAnalyzerFactory.getInstance().getRemoteLandmarkAnalyzer(): creates a landmark analyzer.
    MLRemoteLandmarkAnalyzerSetting.Factory.setLargestNumOfReturns(): sets the maximum number of recognition results that can be returned.
    MLRemoteLandmarkAnalyzerSetting.Factory.setPatternType(): sets the recognition mode.
    MLRemoteLandmarkAnalyzer.asyncAnalyseFrame(MLFrame frame): recognizes landmarks in images.

    6. Text Translation
    MLTranslatorFactory.getInstance().getRemoteTranslator(): creates a translator.
    MLRemoteTranslateSetting.Factory.setSourceLangId(): sets a source language ID.
    MLRemoteTranslateSetting.Factory.setTargetLangId(): sets a target language ID.
    MLRemoteTranslator.asyncTranslate(String sourceText): translates text from the source language to the desired language. sourceText indicates the text to be translated.

    7. Language Detection
    MLLangDetectorFactory.getInstance().getRemoteLangDetector(): creates a language detector.
    MLRemoteLangDetectorSetting.Factory.setTrustedThreshold(): sets the minimum confidence threshold for language detection.
    MLRemoteLangDetector.firstBestDetect(String sourceText): returns the language code with the highest confidence.
    MLRemoteLangDetector.probabilityDetect(String sourceText): returns all possible language codes. sourceText indicates the text to be detected.

    8. Product Visual Search
    MLAnalyzerFactory.getInstance().getRemoteProductVisionSearchAnalyzer(): creates a product visual search analyzer.
    MLRemoteProductVisionSearchAnalyzerSetting.Factory.setLargestNumOfReturns(): sets the maximum number of products that can be returned.
    MLRemoteProductVisionSearchAnalyzer.asyncAnalyseFrame(MLFrame frame): parses all product information in images.

    9. Image Segmentation
    MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(): creates an image segmentation analyzer.
    MLImageSegmentationSetting.Factory.setExact(boolean isExact): sets the detection mode. The value true indicates fine detection, and the value false indicates fast detection.
    MLImageSegmentationAnalyzer.asyncAnalyseFrame(MLFrame frame): detects all objects in images.
    LensEngine: camera source that used for generating continuous image data for detection.

    10. ID Card Recognition
    MLCnIcrCapture.Callback(){}: creates a recognition result callback function and reloads the onSuccess, onCanceled, onFailure, and onDenied functions.
    onSuccess(MLCnIcrCapture Result, idCard Result){}: obtains the recognition result notification. You can add the recognition result processing logic to this method.
    MLCnIcrCaptureConfig.Factory().setFront(boolean isFront).setRemote(boolean isRemote).create(): creates an ID card recognition configurator.
    MLCnIcrCaptureFactory.getInstance().getIcrCapture(this.config): creates an ID card recognition analyzer using the ID card recognition configurator.
    MLCnIcrCapture.capture(MLCnIcrCapture.CallBack callback, Context context): calls the recognition API to obtain ID card information.
    MLCardAnalyzerFactory.getInstance().getIcrAnalyzer(): creates an ID card recognition analyzer.
    MLIcrAnalyzerSetting.Factory().setSideType(MLIcrAnalyzerSetting.FRONT): sets the front or back side of an ID card.
    MLRemoteIcrAnalyzer.asyncAnalyseFrame(MLFrame frame): uses an on-cloud API to recognize ID card information in images.
    MLIcrAnalyzer.asyncAnalyseFrame(MLFrame frame): uses an on-device API to recognize ID card information in images.

    11. Bank Card Recognition
    MLBcrCapture.Callback(){}: creates a recognition result callback function and reloads the onSuccess, onCanceled, onFailure, and onDenied functions.
    onSuccess(MLBcrCaptureResult cardResult){}: obtains the recognition result notification. You can add the recognition result processing logic to this method.
    MLBcrCaptureConfig.Factory().setFront(boolean isFront).setRemote(boolean isRemote).create(): creates a bank card recognition configurator.
    MLBcrCaptureFactory.getInstance().getBcrCapture(MLBcrCaptureConfig config): creates a bank card recognition analyzer using the bank card recognition configurator.
    MLBcrCapture.captureFrame(Context context,MLBcrCapture.Callback callback): calls the recognition API to obtain bank card information.

    12. General Card Recognition
    MLGcrCapture.Callback(){}: creates a recognition result callback function and reloads the onResult, onCanceled, onFailure, and onDenied functions.
    onResult(MLGcrCaptureResult result,Object object){}: obtains the recognition result notification. You can add the recognition result processing logic to this method.
    MLGcrCaptureConfig.Factory().create(): creates a general card recognition configurator.
    MLGcrCaptureUIConfig.Factory().setScanBoxCornerColor(Color.BLUE): sets the color of the general card recognition UI.
    MLGcrCaptureUIConfig.Factory().setTipText("Take a photo,Align edges"): sets the messages displayed on the general card recognition UI.
    MLGcrCaptureUIConfig.Factory().setOrientation(MLGcrCaptureUIConfig.ORIENTATION_AUTO): sets the display mode of the general card recognition UI.
    MLGcrCaptureFactory.getInstance().getGcrCapture(MLGcrCaptureConfig config, MLGcrCaptureUIConfig uiConfig): creates a general card recognition analyzer.
    MLGcrCapture.capturePhoto(Context context, Object object, MLGcrCapture.Callback callback): calls the recognition API to obtain general card information.

    13. ASR
    MLAsrCaptureConstants.ASR_SUCCESS: The ASR plug-in is running properly.
    MLAsrCaptureConstants.ASR_FAILURE: The ASR plug-in is not running properly.

    14. TTS
    MLTtsCallback(){}: creates a TTS callback method to implement the onError, onRangeStart, and onEvent APIs and receives TTS events.
    MLTtsConfig(): creates a TTS configurator to set parameters.
    MLTtsEngine(MLTtsConfig config): creates a TTS engine using the configurator.
    MLTtsEngine.setTtsCallback(): sets a callback method for the TTS engine.

## Preparation
### 1. Register as a developer.
    Before you get started, you must register as a HUAWEI developer and complete identity verification on [HUAWEI Developers](https://developer.huawei.com/consumer/en/). For details, please refer to [Registration and Verification](https://developer.huawei.com/consumer/en/doc/start/10104).
### 2. Create an app and apply for a agconnect-services.json.
	Create an app and set package type to APK (android app). Apply for the agconnect-services.json file on HUAWEI Developers. For details, please refer to [Adding the AppGallery Connect Configuration File.](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc).
### 3. Build
    To build this sample, import the sample to Android Studio (3.x +), download agconnect-services.json from AppGallery Connect, and add the file to the app's root directory app of the demo app.

## Installation
    Download the sample code and open it in Android Studio. Ensure that your device has been connected to the internet and obtain the APK by building a project.

## Function Use
    You can tap buttons in your app to experience rich services of HUAWEI ML Kit.

## Supported Environments
	Devices with Android 4.4 or later are recommended.

##  License
    The sample of HUAWEI ML Kit has obtained the [Apache 2.0 license.](http://www.apache.org/licenses/LICENSE-2.0).