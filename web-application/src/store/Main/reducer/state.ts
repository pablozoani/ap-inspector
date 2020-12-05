import * as Scenes from "../../../containers/scenes/scenes"

export interface MainState {
    currentScene: Scenes.Scene
}

export const initialState: MainState = {
    currentScene: Scenes.HOME
}
