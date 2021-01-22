import Category from "../model/Category"
import Product from "../model/Product"
import Vendor from "../model/Vendor"

/* Order: Alphabetical */

export const GET_CATEGORIES = "GET_CATEGORIES"
export interface FindAllCategoriesAction {
    readonly type: typeof GET_CATEGORIES
    readonly categories: Category[]
}

export const GET_PRODUCTS = "GET_PRODUCTS"
export interface FindAllProductsAction {
    readonly type: typeof GET_PRODUCTS
    readonly products: Product[]
}

export const GET_VENDORS = "GET_VENDORS"
export interface FindAllVendorsAction {
    readonly type: typeof GET_VENDORS
    readonly vendors: Vendor[]
}

export const MOVE_PAGE_FIRST = "MOVE_PAGE_FIRST"
export interface MovePageFirstAction {
    readonly type: typeof MOVE_PAGE_FIRST
    readonly products: Product[]
}

export const MOVE_PAGE_LAST = "MOVE_PAGE_LAST"
export interface MovePageLastAction {
    readonly type: typeof MOVE_PAGE_LAST
    readonly page: number
    readonly products: Product[]
}

export const MOVE_PAGE_LEFT = "MOVE_PAGE_LEFT"
export interface MovePageLeftAction {
    readonly type: typeof MOVE_PAGE_LEFT
    readonly products: Product[]
}

export const MOVE_PAGE_RIGHT = "MOVE_PAGE_RIGHT"
export interface MovePageRightAction {
    readonly type: typeof MOVE_PAGE_RIGHT
    readonly products: Product[]
}

export const SET_CURRENT_CATEGORY_ID = "SET_CURRENT_CATEGORY_ID"
export interface SetCurrentCategoryIdAction {
    readonly type: typeof SET_CURRENT_CATEGORY_ID
    readonly categoryId: string
}

export const SET_CURRENT_SEARCH_KEY = "SET_CURRENT_SEARCH_KEY"
export interface SetCurrentSearchKeyAction {
    readonly type: typeof SET_CURRENT_SEARCH_KEY
    readonly searchKey: string
}

export const SET_CURRENT_VENDOR_ID = "SET_CURRENT_VENDOR_ID"
export interface SetCurrentVendorIdAction {
    readonly type: typeof SET_CURRENT_VENDOR_ID
    readonly vendorId: string
}

export const SET_SORT_BY = "SET_SORT_BY"
export interface SetSortByAction {
    readonly type: typeof SET_SORT_BY
    readonly sortType: string
}

export type HomeActionTypes =
    | FindAllVendorsAction
    | FindAllProductsAction
    | MovePageRightAction
    | MovePageLeftAction
    | MovePageFirstAction
    | MovePageLastAction
    | SetSortByAction
    | SetCurrentVendorIdAction
    | FindAllCategoriesAction
    | SetCurrentCategoryIdAction
    | SetCurrentSearchKeyAction;
