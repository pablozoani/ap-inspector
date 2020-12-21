import { combineReducers } from "redux"
import mainReducer from "./Main/reducer/reducer"
import homeReducer from "./Home/reducer/reducer"

export const rootReducer = combineReducers({
    main: mainReducer,
    home: homeReducer
})

export type RootState = ReturnType<typeof rootReducer>
