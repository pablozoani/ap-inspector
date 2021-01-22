import { combineReducers } from "redux"
import homeReducer from "./homeReducer"
import mainReducer from "./sceneReducer"

export const rootReducer = combineReducers({
    main: mainReducer,
    home: homeReducer
})

export type RootState = ReturnType<typeof rootReducer>
