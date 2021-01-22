import * as ActionTypes from "../types/sceneTypes";
import * as State from "./states/sceneState";

export default (
    state: State.MainState = State.initialState,
    action: ActionTypes.MainActionTypes
) => {
    switch (action.type) {
        case ActionTypes.SET_SCENE:
            return {
                ...state,
                currentScene: action.scene
            };
        default:
            return state;
    }
};
