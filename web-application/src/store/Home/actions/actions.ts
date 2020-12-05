import axios, { AxiosResponse } from "axios"
import Product from "../model/Product"
import { Dispatch } from "redux"
import {
    FindAllCategoriesAction,
    FindAllProductsAction,
    FindAllVendorsAction,
    GET_CATEGORIES,
    GET_PRODUCTS,
    GET_VENDORS,
    MovePageFirstAction,
    MovePageLastAction,
    MovePageLeftAction,
    MovePageRightAction,
    MOVE_PAGE_FIRST,
    MOVE_PAGE_LAST,
    MOVE_PAGE_LEFT,
    MOVE_PAGE_RIGHT,
    SetCurrentCategoryIdAction,
    SetCurrentSearchKeyAction,
    SetCurrentVendorIdAction,
    SetSortByAction,
    SET_CURRENT_CATEGORY_ID,
    SET_CURRENT_SEARCH_KEY,
    SET_CURRENT_VENDOR_ID,
    SET_SORT_BY
} from "./types/types"
import { RootState } from "../../rootReducer"
import Vendor from "../model/Vendor"
import Category from "../model/Category"
import { API_BASE_URL } from "../../constants"

export const findAllVendors = () => (
    dispatch: Dispatch<FindAllVendorsAction>
): void => {
    axios
        .get<Vendor[]>(API_BASE_URL + "/api/v1/vendors")
        .then((response) => {
            dispatch<FindAllVendorsAction>({
                type: GET_VENDORS,
                vendors: response.data
            })
        })
        .catch((error) => console.log(error))
}

export const findAllCategories = () => (
    dispatch: Dispatch<FindAllCategoriesAction>
): void => {
    axios
        .get<Category[]>(API_BASE_URL + "/api/v1/categories")
        .then((response) => {
            dispatch<FindAllCategoriesAction>({
                type: GET_CATEGORIES,
                categories: response.data
            })
        })
        .catch((error) => console.log(error))
}

export const findAllProducts = () => (
    dispatch: Dispatch<FindAllProductsAction>,
    getState: () => RootState
): void => {
    axios
        .get<Product[]>(
            API_BASE_URL +
                "/api/v1/products?" +
                "sortBy=" +
                getState().home.sortBy[0] +
                "&" +
                "sortOrder=" +
                getState().home.sortBy[1] +
                "&" +
                "page=" +
                0 +
                "&" +
                "size=" +
                getState().home.pageSize +
                "&" +
                "vendorId=" +
                getState().home.currentVendorId +
                "&" +
                "categoryId=" +
                getState().home.currentCategoryId +
                "&" +
                "productNameLike=" +
                getState().home.currentSearchKey
        )
        .then((response) => {
            dispatch<FindAllProductsAction>({
                type: GET_PRODUCTS,
                products: response.data
            })
        })
        .catch((error) => console.log(error))
}

export const movePageRight = () => (
    dispatch: Dispatch<MovePageRightAction>,
    getState: () => RootState
): void => {
    axios
        .get<Product[]>(
            API_BASE_URL +
                "/api/v1/products?" +
                "sortBy=" +
                getState().home.sortBy[0] +
                "&" +
                "sortOrder=" +
                getState().home.sortBy[1] +
                "&" +
                "page=" +
                (getState().home.currentPage + 1) +
                "&" +
                "size=" +
                getState().home.pageSize +
                "&" +
                "vendorId=" +
                getState().home.currentVendorId +
                "&" +
                "categoryId=" +
                getState().home.currentCategoryId +
                "&" +
                "productNameLike=" +
                getState().home.currentSearchKey
        )
        .then((response) => {
            if (response.data.length > 0) {
                dispatch<MovePageRightAction>({
                    type: MOVE_PAGE_RIGHT,
                    products: response.data
                })
            }
        })
        .catch((error) => console.log(error))
}

export const movePageLeft = () => (
    dispatch: Dispatch<MovePageLeftAction>,
    getState: () => RootState
): void => {
    if (getState().home.currentPage > 0) {
        axios
            .get<Product[]>(
                API_BASE_URL +
                    "/api/v1/products?" +
                    "sortBy=" +
                    getState().home.sortBy[0] +
                    "&" +
                    "sortOrder=" +
                    getState().home.sortBy[1] +
                    "&" +
                    "page=" +
                    (getState().home.currentPage - 1) +
                    "&" +
                    "size=" +
                    getState().home.pageSize +
                    "&" +
                    "vendorId=" +
                    getState().home.currentVendorId +
                    "&" +
                    "categoryId=" +
                    getState().home.currentCategoryId +
                    "&" +
                    "productNameLike=" +
                    getState().home.currentSearchKey
            )
            .then((response) => {
                if (response.data.length > 0) {
                    dispatch<MovePageLeftAction>({
                        type: MOVE_PAGE_LEFT,
                        products: response.data
                    })
                }
            })
            .catch((error) => console.log(error))
    }
}

export const movePageFirst = () => (
    dispatch: Dispatch<MovePageFirstAction>,
    getState: () => RootState
): void => {
    axios
        .get<Product[]>(
            API_BASE_URL +
                "/api/v1/products?" +
                "sortBy=" +
                getState().home.sortBy[0] +
                "&" +
                "sortOrder=" +
                getState().home.sortBy[1] +
                "&" +
                "page=" +
                0 +
                "&" +
                "size=" +
                getState().home.pageSize +
                "&" +
                "vendorId=" +
                getState().home.currentVendorId +
                "&" +
                "categoryId=" +
                getState().home.currentCategoryId +
                "&" +
                "productNameLike=" +
                getState().home.currentSearchKey
        )
        .then((response) => {
            dispatch<MovePageFirstAction>({
                type: MOVE_PAGE_FIRST,
                products: response.data
            })
        })
        .catch((error) => console.log(error))
}

export const movePageLast = () => (
    dispatch: Dispatch<MovePageLastAction>,
    getState: () => RootState
) => {
    axios
        .get<number>(
            API_BASE_URL +
                "/api/v1/products/count?" +
                "categoryId=" +
                getState().home.currentCategoryId +
                "&" +
                "vendorId=" +
                getState().home.currentVendorId +
                "&" +
                "productNameLike=" +
                getState().home.currentSearchKey
        )
        .then((response) => {
            const page = Math.trunc(
                (response.data - 0.01) / getState().home.pageSize
            )
            axios
                .get<Product[]>(
                    API_BASE_URL +
                        "/api/v1/products?" +
                        "sortBy=" +
                        getState().home.sortBy[0] +
                        "&" +
                        "sortOrder=" +
                        getState().home.sortBy[1] +
                        "&" +
                        "page=" +
                        page +
                        "&" +
                        "size=" +
                        getState().home.pageSize +
                        "&" +
                        "vendorId=" +
                        getState().home.currentVendorId +
                        "&" +
                        "categoryId=" +
                        getState().home.currentCategoryId +
                        "&" +
                        "productNameLike=" +
                        getState().home.currentSearchKey
                )
                .then((res) => {
                    dispatch<MovePageLastAction>({
                        type: MOVE_PAGE_LAST,
                        page,
                        products: res.data
                    })
                })
                .catch((error) => console.log(error))
        })
        .catch((error) => console.log(error))
}

export const setCurrentCategoryId = (
    categoryId: string
): SetCurrentCategoryIdAction => {
    return { type: SET_CURRENT_CATEGORY_ID, categoryId }
}

export const setCurrentVendorId = (
    vendorId: string
): SetCurrentVendorIdAction => {
    return { type: SET_CURRENT_VENDOR_ID, vendorId }
}

export const setCurrentSearchKey = (
    searchKey: string
): SetCurrentSearchKeyAction => {
    return { type: SET_CURRENT_SEARCH_KEY, searchKey }
}

export const setSortBy = (sortType: string): SetSortByAction => {
    return { type: SET_SORT_BY, sortType }
}
