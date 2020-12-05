import { Scene } from "../../../../containers/scenes/scenes"

export const SET_SCENE = "SET_SCENE"

export interface SetSceneAction {
    readonly type: typeof SET_SCENE
    readonly scene: Scene
}

export type MainActionTypes = SetSceneAction
