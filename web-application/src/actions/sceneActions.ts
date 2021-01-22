import { Scene } from "../containers/scenes/scenes";
import { SetSceneAction, SET_SCENE } from "../types/sceneTypes";

export const setScene = (scene: Scene): SetSceneAction => {
    return {
        type: SET_SCENE,
        scene
    };
};
