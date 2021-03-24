package controllers;

import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import util.CoordinatePair;

import java.util.*;
import java.util.stream.Collectors;

public class ScrollPaneController
{
    @FXML
    private SubScene drawingSubScene;
    private final Group root = new Group();

    private final List<Circle> pointsOfFigures = new ArrayList<>();
    private final Queue<CoordinatePair> mouseClicksCoordinatesToDrawLine = new ArrayDeque<>();
    private final Queue<CoordinatePair> mouseClicksCoordinatesToDrawCircle = new ArrayDeque<>();
    private final Queue<CoordinatePair> mouseClicksCoordinatesToDrawRectangle = new ArrayDeque<>();
    private final Queue<CoordinatePair> mouseClicksCoordinatesToDrawTriangle = new ArrayDeque<>();
    private final Queue<CoordinatePair> mouseClicksCoordinatesToDrawQuadraticPolygon = new ArrayDeque<>();

    private static boolean isFirstClickOnSubScene = true;

    public void setRootOfSubScene()
    {
        drawingSubScene.setRoot(root);
        /*
         * This method only get executed if it's the first click on the SubScene, and I
         * only want this method to be executed one time, so I need to establish that
         * the first click on the SubScene is already gone
         * */
        isFirstClickOnSubScene = false;
    }

    public void defineWhatToDo(MouseEvent mouseEvent)
    {
        /*
         * The user can click on the SubScene before clicking on one
         * of the buttons, so I need to check if he already clicked
         * And if so, one of the drawing modes must be active
         * */
        if (VBoxController.isDrawingModeActive()) drawFigure(mouseEvent);
        else if (VBoxController.isEraserModeActive)
            if (!createEraser(mouseEvent))
                changeEraserPosition(mouseEvent);
    }

    /*
     * This method only is executed if the user clicks on the SubScene
     * */
    public void drawFigure(MouseEvent mouseEvent)
    {
        if (isFirstClickOnSubScene) setRootOfSubScene();

        CoordinatePair pointCoordinates = new CoordinatePair(mouseEvent.getX(), mouseEvent.getY());
        if (VBoxController.isDrawingLineModeActive)
        {
            mouseClicksCoordinatesToDrawLine.add(pointCoordinates);
            drawPoint(pointCoordinates);
            if (mouseClicksCoordinatesToDrawLine.size() == 2)
            {
                drawLine();
                root.getChildren().removeAll(pointsOfFigures);
            }
        }
        else if (VBoxController.isDrawingCircleModeActive)
        {
            mouseClicksCoordinatesToDrawCircle.add(pointCoordinates);
            drawPoint(pointCoordinates);
            if (mouseClicksCoordinatesToDrawCircle.size() == 2)
            {
                drawCircle();
                root.getChildren().removeAll(pointsOfFigures);
            }
        }
        else if (VBoxController.isDrawingRectangleModeActive)
        {
            mouseClicksCoordinatesToDrawRectangle.add(pointCoordinates);
            drawPoint(pointCoordinates);
            if (mouseClicksCoordinatesToDrawRectangle.size() == 2)
            {
                drawRectangle();
                root.getChildren().removeAll(pointsOfFigures);
            }
        }
        else if (VBoxController.isDrawingTriangleModeActive)
        {
            mouseClicksCoordinatesToDrawTriangle.add(pointCoordinates);
            drawPoint(pointCoordinates);
            if (mouseClicksCoordinatesToDrawTriangle.size() == 3)
            {
                drawTriangle();
                root.getChildren().removeAll(pointsOfFigures);
            }
        }
        else if (VBoxController.isDrawingConcavePolygonModeActive)
        {
            mouseClicksCoordinatesToDrawQuadraticPolygon.add(pointCoordinates);
            drawPoint(pointCoordinates);
            if (mouseClicksCoordinatesToDrawQuadraticPolygon.size() == 4)
            {
                drawConcaveQuadraticPolygon();
                root.getChildren().removeAll(pointsOfFigures);
            }
        }
        else if (VBoxController.isDrawingFractalsModeActive) drawBarnsleyFern(mouseEvent);
    }

    public List<CoordinatePair> getCornersCoordinates(String figure)
    {
        List<CoordinatePair> corners = new ArrayList<>();

        switch (figure)
        {
            case "Line":
                corners = List.of(
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawLine.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawLine.poll()));
                break;
            case "Triangle":
                corners = List.of(
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawTriangle.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawTriangle.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawTriangle.poll()));
                break;
            case "Rectangle":
                corners = List.of(
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawRectangle.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawRectangle.poll()));
                break;
            case "Quadratic Concave Polygon":
                corners = List.of(
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawQuadraticPolygon.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawQuadraticPolygon.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawQuadraticPolygon.poll()),
                        Objects.requireNonNull(mouseClicksCoordinatesToDrawQuadraticPolygon.poll()));
                break;
        }
        return corners;
    }

    public void drawPoint(CoordinatePair pointCoordinates)
    {
        Circle point = new Circle();
        point.setCenterX(pointCoordinates.getX());
        point.setCenterY(pointCoordinates.getY());
        point.setFill(Color.BLACK);
        point.setRadius(1);
        pointsOfFigures.add(point);
        root.getChildren().add(point);
    }

    public void drawLine()
    {
        List<CoordinatePair> corners = getCornersCoordinates("Line");
        Line line = new Line();
        line.setStartX(corners.get(0).getX());
        line.setStartY(corners.get(0).getY());
        line.setEndX(corners.get(1).getX());
        line.setEndY(corners.get(1).getY());
        line.setStroke(VBoxController.colorPicker.getValue());
        line.setStrokeWidth(Double.parseDouble((String) VBoxController.thicknessChoiceBox.getValue()));
        root.getChildren().add(line);
    }

    public void drawCircle()
    {
        CoordinatePair centerOfTheCircle = mouseClicksCoordinatesToDrawCircle.poll();
        CoordinatePair extremityOfTheCircle = mouseClicksCoordinatesToDrawCircle.poll();
        Circle circle = new Circle();
        circle.setCenterX(Objects.requireNonNull(centerOfTheCircle).getX());
        circle.setCenterY(centerOfTheCircle.getY());
        circle.setRadius(Math.abs(Objects.requireNonNull(extremityOfTheCircle).getX() - centerOfTheCircle.getX()));
        circle.setFill(Color.BLACK);
        root.getChildren().add(circle);
    }

    public void drawRectangle()
    {
        List<CoordinatePair> corners = getCornersCoordinates("Rectangle");
        Rectangle rectangle = new Rectangle();
        if (corners.get(0).getX() < corners.get(1).getX() && corners.get(0).getY() > corners.get(1).getY())
        {
            rectangle.setX(corners.get(0).getX());
            rectangle.setY(corners.get(1).getY());
        }
        else if (corners.get(0).getX() < corners.get(1).getX() && corners.get(0).getY() < corners.get(1).getY())
        {
            rectangle.setX(corners.get(0).getX());
            rectangle.setY(corners.get(0).getY());
        }
        else if (corners.get(0).getX() > corners.get(1).getX() && corners.get(0).getY() > corners.get(1).getY())
        {
            rectangle.setX(corners.get(1).getX());
            rectangle.setY(corners.get(1).getY());
        }
        else if (corners.get(0).getX() > corners.get(1).getX() && corners.get(0).getY() < corners.get(1).getY())
        {
            rectangle.setX(corners.get(1).getX());
            rectangle.setY(corners.get(0).getY());
        }
        rectangle.setWidth(Math.abs(corners.get(0).getX() - corners.get(1).getX()));
        rectangle.setHeight(Math.abs(corners.get(0).getY() - corners.get(1).getY()));
        rectangle.setFill(VBoxController.colorPicker.getValue());
        root.getChildren().add(rectangle);
    }

    public void drawTriangle()
    {
        List<CoordinatePair> corners = getCornersCoordinates("Triangle");
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                corners.get(0).getX(), corners.get(0).getY(),
                corners.get(1).getX(), corners.get(1).getY(),
                corners.get(2).getX(), corners.get(2).getY());
        triangle.setFill(VBoxController.colorPicker.getValue());
        root.getChildren().add(triangle);
    }

    public void drawConcaveQuadraticPolygon()
    {
        List<CoordinatePair> corners = getCornersCoordinates("Quadratic Concave Polygon");
        Polygon concaveQuadraticPolygon = new Polygon();
        concaveQuadraticPolygon.getPoints().addAll(
                corners.get(0).getX(), corners.get(0).getY(),
                corners.get(1).getX(), corners.get(1).getY(),
                corners.get(2).getX(), corners.get(2).getY(),
                corners.get(3).getX(), corners.get(3).getY());
        concaveQuadraticPolygon.setFill(VBoxController.colorPicker.getValue());
        root.getChildren().add(concaveQuadraticPolygon);
    }

    public void drawBarnsleyFern(MouseEvent mouseEvent)
    {
        double xCoordinate = mouseEvent.getX();
        double yCoordinate = mouseEvent.getY();
        double storedX;
        double storedY;
        Random randomizer = new Random();
        double randomNumber;

        for (int i = 0; i < 110000; i++)
        {
            Circle point = new Circle(65 * xCoordinate, 37 * yCoordinate - 252, 1);
            point.setFill(Color.GREEN);
            root.getChildren().add(point);

            randomNumber = randomizer.nextDouble() * 100;
            storedX = xCoordinate;
            storedY = yCoordinate;

            if (randomNumber < 1)
            {
                xCoordinate = 0;
                yCoordinate = 0.16 * storedY;
            }
            else if (randomNumber < 86)
            {
                xCoordinate = 0.85 * storedX + 0.04 * storedY;
                yCoordinate = -0.04 * storedX + 0.85 * storedY + 1.6;
            }
            else if (randomNumber < 93)
            {
                xCoordinate = 0.20 * storedX - 0.26 * storedY;
                yCoordinate = 0.23 * storedX + 0.22 * storedY + 1.6;
            }
            else
            {
                xCoordinate = -0.15 * storedX + 0.28 * storedY;
                yCoordinate = 0.26 * storedX + 0.24 * storedY + 0.44;
            }
        }
    }

    public void dragFigure(MouseEvent mouseEvent)
    {
//        disableDrawingMode();
        EventTarget target = mouseEvent.getTarget();
        if (target instanceof Line)
        {
            double lineHeadX = ((Line) target).getStartX();
            double lineHeadY = ((Line) target).getStartY();
            double lineTailX = ((Line) target).getEndX();
            double lineTailY = ((Line) target).getEndY();

            double differenceLineHeadMouseX = Math.abs(Math.round(lineHeadX - mouseEvent.getX()));
            double differenceLineHeadMouseY = Math.abs(Math.round(lineHeadY - mouseEvent.getY()));
            double differenceLineTailMouseX = Math.abs(Math.round(lineTailX - mouseEvent.getX()));
            double differenceLineTailMouseY = Math.abs(Math.round(lineTailY - mouseEvent.getY()));

            if (differenceLineHeadMouseX < 20 && differenceLineHeadMouseY < 20)
            {
                ((Line) target).setStartX(mouseEvent.getX());
                ((Line) target).setStartY(mouseEvent.getY());
            }
            else if (differenceLineTailMouseX < 20 && differenceLineTailMouseY < 20)
            {
                ((Line) target).setEndX(mouseEvent.getX());
                ((Line) target).setEndY(mouseEvent.getY());
            }
        }
        else if (target instanceof Circle)
        {
            ((Circle) target).setCenterX(mouseEvent.getX());
            ((Circle) target).setCenterY(mouseEvent.getY());
        }
        else if (target instanceof Rectangle)
        {
            ((Rectangle) target).setX(mouseEvent.getX());
            ((Rectangle) target).setY(mouseEvent.getY());
        }
        else if (target instanceof Polygon)
        {
            if (((Polygon) target).getPoints().size() == 6)
            {
                for (int index = 0; index < ((Polygon) target).getPoints().size(); index++)
                {
                    Polygon polygonTarget = (Polygon) target;
                    double xValue = polygonTarget.getPoints().get(index) + polygonTarget.getPoints().get(index) - mouseEvent.getX();
                    double yValue = polygonTarget.getPoints().get(index) + polygonTarget.getPoints().get(index) - mouseEvent.getY();
                    // X coordinates
                    if (index % 2 == 0)
                        polygonTarget.getPoints().set(index, xValue);
                        // Y coordinates
                    else
                        polygonTarget.getPoints().set(index, yValue);
                }
            }
        }
    }

    public boolean createEraser(MouseEvent mouseEvent)
    {
        /*
         * This verification prevents the system from creating more than
         * one eraser
         * */
        if (root.getChildren()
                .stream()
                .noneMatch(figure ->
                {
                    if (figure.getId() != null) return figure.getId().equals("eraser");
                    return false;
                }))
        {
            Circle eraser = new Circle();
            eraser.setId("eraser");
            eraser.getStyleClass().add("eraser");
            eraser.setCenterX(mouseEvent.getX());
            eraser.setCenterY(mouseEvent.getY());
            eraser.setRadius(Double.parseDouble((String) VBoxController.thicknessChoiceBox.getValue()));
            eraser.setFill(Color.RED);

            root.getChildren().add(eraser);

            return true;
        }
        return false;
    }

    public void changeEraserPosition(MouseEvent mouseEvent)
    {
        List<Node> erasers = root.getChildren()
                .stream()
                .filter(figure ->
                {
                    if (figure.getId() != null) return figure.getId().equals("eraser");
                    return false;
                })
                .collect(Collectors.toList());

        if (erasers.size() == 1)
        {
            System.out.println("Erasers.size() == 1");
            Circle eraser = (Circle) erasers.get(0);
            eraser.setCenterX(mouseEvent.getX());
            eraser.setCenterY(mouseEvent.getY());

            erasePieceOfFigure(eraser, root);
        }
    }

    private void erasePieceOfFigure(Circle eraser, Group root)
    {
        System.out.println("Inside method erasePieceOfFigure");
        Object[] figures = root.getChildren()
                .stream()
                .filter(figure ->
                        figure.getId() == null || !figure.getId().equals("eraser"))
                .toArray();

        for (Object figure : figures)
        {
            if (figure instanceof Circle)
            {
                if (eraser.intersects(((Circle) figure).getBoundsInLocal()))
                {
                    System.out.println("Eraser intersected a circle");
                    ((Circle) figure).setFill(eraser.getFill());
                }
            }
        }
    }
}
