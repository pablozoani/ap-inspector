import React, { useEffect, useState } from "react";
import AddProduct from "./Home/AddProduct/AddProduct";
import * as Scenes from "./scenes/scenes";
import Home from "./Home/Home";
import { useSelector } from "react-redux";
import { RootState } from "../reducers";
import classes from "./Main.module.css";

type ContainerProps = {};

const Container: React.FC<ContainerProps> = (props) => {
    return <div className={classes.Container}>{props.children}</div>;
};

type MainProps = {};

const Main: React.FC<MainProps> = (props) => {
    const [sceneToRender, setSceneToRender] = useState(<Home />);
    const currentScene = useSelector((state: RootState) => state.main.currentScene);
    useEffect(() => {
        switch (currentScene) {
            case Scenes.ADD_PRODUCT:
                setSceneToRender(<AddProduct />);
                break;
            case Scenes.HOME:
                setSceneToRender(<Home />);
                break;
            default:
                throw new Error("Invalid Scene");
        }
    }, [currentScene]);

    return (
        <div>
            <Container>{sceneToRender}</Container>
        </div>
    );
};

export default Main;
