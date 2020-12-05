import {
    GET_CATEGORIES,
    GET_PRODUCTS,
    GET_VENDORS,
    HomeActionTypes,
    MOVE_PAGE_FIRST,
    MOVE_PAGE_LAST,
    MOVE_PAGE_LEFT,
    MOVE_PAGE_RIGHT,
    SET_CURRENT_CATEGORY_ID,
    SET_CURRENT_SEARCH_KEY,
    SET_CURRENT_VENDOR_ID,
    SET_SORT_BY
} from "../actions/types/types"
import { HomeState, initialState } from "./state"

export default (
    state: HomeState = initialState,
    action: HomeActionTypes
): HomeState => {
    switch (action.type) {
        /* filters */
        case SET_CURRENT_CATEGORY_ID:
            return {
                ...state,
                currentCategoryId: action.categoryId
            }
        case SET_CURRENT_VENDOR_ID:
            return {
                ...state,
                currentVendorId: action.vendorId
            }
        case SET_CURRENT_SEARCH_KEY:
            return {
                ...state,
                currentSearchKey: action.searchKey
            }
        /* paging and sorting */
        case SET_SORT_BY:
            return {
                ...state,
                sortBy: action.sortType.split(" ")
            }
        case MOVE_PAGE_LEFT:
            return {
                ...state,
                currentPage: state.currentPage - 1,
                products: action.products
            }
        case MOVE_PAGE_RIGHT:
            return {
                ...state,
                currentPage: state.currentPage + 1,
                products: action.products
            }
        case MOVE_PAGE_FIRST:
            return {
                ...state,
                currentPage: 0,
                products: action.products
            }
        case MOVE_PAGE_LAST:
            return {
                ...state,
                currentPage: action.page,
                products: action.products
            }
        /* model updates */
        case GET_PRODUCTS:
            return {
                ...state,
                products: action.products,
                currentPage: 0
            }
        case GET_VENDORS:
            return {
                ...state,
                vendors: action.vendors
            }
        case GET_CATEGORIES:
            return {
                ...state,
                categories: action.categories
            }
        default:
            return state
    }
}
