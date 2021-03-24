package controllers;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;

public class VBoxController
{
    public static boolean isDrawingLineModeActive = false;
    public static boolean isDrawingCircleModeActive = false;
    public static boolean isDrawingRectangleModeActive = false;
    public static boolean isDrawingTriangleModeActive = false;
    public static boolean isDrawingConcavePolygonModeActive = false;
    public static boolean isDrawingFractalsModeActive = false;
    public static boolean isEraserModeActive = false;

    public static ColorPicker colorPicker;
    public static ChoiceBox thicknessChoiceBox;

    public void activateDrawLineMode()
    {
        isDrawingLineModeActive = true;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = false;
    }

    public void activateDrawCircleMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = true;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = false;
    }

    public void activateDrawRectangleMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = true;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = false;
    }

    public void activateDrawTriangleMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = true;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = false;
    }

    public void activateDrawConcaveQuadraticPolygonMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = true;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = false;
    }

    public void activateDrawFractalsMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = true;
        isEraserModeActive = false;
    }

    public void activateEraserMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = true;
    }

    public void disableDrawingMode()
    {
        isDrawingLineModeActive = false;
        isDrawingCircleModeActive = false;
        isDrawingRectangleModeActive = false;
        isDrawingTriangleModeActive = false;
        isDrawingConcavePolygonModeActive = false;
        isDrawingFractalsModeActive = false;
        isEraserModeActive = false;
    }

    public static boolean isDrawingModeActive()
    {
        return isDrawingLineModeActive || isDrawingCircleModeActive
                || isDrawingRectangleModeActive || isDrawingTriangleModeActive
                || isDrawingFractalsModeActive || isDrawingConcavePolygonModeActive
                || !isEraserModeActive;
    }
}
