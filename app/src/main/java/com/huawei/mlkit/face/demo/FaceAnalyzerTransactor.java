package com.huawei.mlkit.face.demo;
/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;

import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.common.MLPosition;
import com.huawei.hms.mlsdk.face.MLFace;
import com.huawei.hms.mlsdk.face.MLFaceShape;
import com.huawei.mlkit.face.demo.camera.GraphicOverlay;
import com.huawei.mlkit.face.demo.ui.MLFaceGraphic;

import java.util.ArrayList;
import java.util.List;

public class FaceAnalyzerTransactor implements MLAnalyzer.MLTransactor<MLFace> {
    private GraphicOverlay mGraphicOverlay;
    private String type;
    AssignationModel assignationResponse;
    FaceAnalyzerTransactor(GraphicOverlay ocrGraphicOverlay,String type,AssignationModel assignationResponse) {
        this.mGraphicOverlay = ocrGraphicOverlay;
        this.type = type;
        this.assignationResponse = assignationResponse;
    }

    public interface AssignationModel{
        void successAssignation(FaceModel model);
    }

    @Override
    public void transactResult(MLAnalyzer.Result<MLFace> result) {
        this.mGraphicOverlay.clear();
        SparseArray<MLFace> faceSparseArray = result.getAnalyseList();
        if(type.equals(FaceConstants.SAVE)){
            for (int i = 0; i < faceSparseArray.size(); i++) {
                FaceModel faceModel = new FaceModel();
                List<MLPosition> mlPositions =  faceSparseArray.get(i).getAllPoints();
                List<PositionObj> posListStore = new ArrayList<>();
                for (MLPosition position:mlPositions) {

                    PositionObj positionObj = new PositionObj();
                    positionObj.x = position.getX();
                    positionObj.y = position.getY();
                    posListStore.add(positionObj);
                }
                float borderBottom = faceSparseArray.get(i).getBorder().bottom;
                float borderTop = faceSparseArray.get(i).getBorder().top;
                float borderLeft = faceSparseArray.get(i).getBorder().left;
                float borderRight = faceSparseArray.get(i).getBorder().right;
                float xPointCoord = faceSparseArray.get(i).getCoordinatePoint().x;
                float yPointCoord = faceSparseArray.get(i).getCoordinatePoint().y;
                float faceHeight = faceSparseArray.get(i).getHeight();
                float faceWidth = faceSparseArray.get(i).getWidth();
                int age = faceSparseArray.get(i).getFeatures().getAge();
                float mustache = faceSparseArray.get(i).getFeatures().getMoustacheProbability();
                float sex = faceSparseArray.get(i).getFeatures().getSexProbability();
                faceModel.age = age;
                faceModel.borderBottom = borderBottom;
                faceModel.borderLeft = borderLeft;
                faceModel.borderRight = borderRight;
                faceModel.borderTop = borderTop;
                faceModel.xPointCoord = xPointCoord;
                faceModel.yPointCoord = yPointCoord;
                faceModel.faceHeight = faceHeight;
                faceModel.faceWidth = faceWidth;
                faceModel.mustache = mustache;
                faceModel.sex = sex;

                assignationResponse.successAssignation(faceModel);

                //MLFaceGraphic graphic = new MLFaceGraphic(mGraphicOverlay, faceSparseArray.valueAt(i));

            }
        }else {
            for (int i = 0; i < faceSparseArray.size(); i++) {
                // todo step 4: add on-device face graphic
                MLFaceGraphic graphic = new MLFaceGraphic(mGraphicOverlay, faceSparseArray.valueAt(i));
                mGraphicOverlay.add(graphic);
                // finish
            }
        }

    }

    @Override
    public void destroy() {
        this.mGraphicOverlay.clear();
    }

}