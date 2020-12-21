import * as ActionTypes from "../actions/types/types"
import * as State from "./state"

export default (
    state: State.MainState = State.initialState,
    action: ActionTypes.MainActionTypes
) => {
    switch (action.type) {
        case ActionTypes.SET_SCENE:
            return {
                ...state,
                currentScene: action.scene
            }
        default:
            return state
    }
}
