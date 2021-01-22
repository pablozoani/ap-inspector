import { Dispatch } from "redux"
import * as aTypes from "../types/homeTypes"
import { RootState } from "../reducers"
import { getVendors } from "../api/vendorsAPI"
import { getCategories } from "../api/categoriesAPI"
import { getProductCount, getProducts } from "../api/productsAPI"

export const findAllVendors = () => async (
    dispatch: Dispatch<aTypes.FindAllVendorsAction>
): Promise<void> => {
    try {
        const { data } = await getVendors();
        dispatch<aTypes.FindAllVendorsAction>({
            type: aTypes.GET_VENDORS,
            vendors: data
        });
    } catch (error) {
        console.log({ error });
    }
}

export const findAllCategories = () => async (
    dispatch: Dispatch<aTypes.FindAllCategoriesAction>
): Promise<void> => {
    try {
        const { data } = await getCategories();
        dispatch({
            type: aTypes.GET_CATEGORIES,
            categories: data
        });
    } catch (error) {
        console.log({ error });
    }
}

export const findAllProducts = () => async (
    dispatch: Dispatch<aTypes.FindAllProductsAction>,
    getState: () => RootState
): Promise<void> => {
    try {
        const { home } = getState();
        const { data } = await getProducts(
            home.sortBy[0],
            home.sortBy[1],
            0,
            home.pageSize,
            home.currentVendorId,
            home.currentCategoryId,
            home.currentSearchKey
        );
        dispatch({
            type: aTypes.GET_PRODUCTS,
            products: data
        })
    } catch (error) {
        console.log({ error });
    }
}

export const movePageRight = () => async (
    dispatch: Dispatch<aTypes.MovePageRightAction>,
    getState: () => RootState
): Promise<void> => {
    try {
        const { home } = getState();
        const { data } = await getProducts(
            home.sortBy[0],
            home.sortBy[1],
            home.currentPage + 1,
            home.pageSize,
            home.currentVendorId,
            home.currentCategoryId,
            home.currentSearchKey
        );
        if (data.length > 0) {
            dispatch({
                type: aTypes.MOVE_PAGE_RIGHT,
                products: data
            });
        }
    } catch (error) {
        console.log({ error });
    }
}

export const movePageLeft = () => async (
    dispatch: Dispatch<aTypes.MovePageLeftAction>,
    getState: () => RootState
): Promise<void> => {
    try {
        const { home } = getState();
        if (!(home.currentPage > 0)) return;
        const { data } = await getProducts(
            home.sortBy[0],
            home.sortBy[1],
            home.currentPage - 1,
            home.pageSize,
            home.currentVendorId,
            home.currentCategoryId,
            home.currentSearchKey
        );
        if (!(data.length > 0)) return;
        dispatch({
            type: aTypes.MOVE_PAGE_LEFT,
            products: data
        });
    } catch (error) {
        console.log({ error });
    }
}

export const movePageFirst = () => async (
    dispatch: Dispatch<aTypes.MovePageFirstAction>,
    getState: () => RootState
): Promise<void> => {
    try {
        const { home } = getState();
        const { data } = await getProducts(
            home.sortBy[0],
            home.sortBy[1],
            0,
            home.pageSize,
            home.currentVendorId,
            home.currentCategoryId,
            home.currentSearchKey
        );
        dispatch({
            type: aTypes.MOVE_PAGE_FIRST,
            products: data
        })
    } catch (error) {
        console.log({ error });
    }
}

export const movePageLast = () => async (
    dispatch: Dispatch<aTypes.MovePageLastAction>,
    getState: () => RootState
) => {
    try {
        const { home } = getState();

        const { data: productCount } = await getProductCount(
            home.currentCategoryId,
            home.currentVendorId,
            home.currentSearchKey
        );

        const page = Math.trunc((productCount - 0.01) / home.pageSize);

        const { data: products } = await getProducts(
            home.sortBy[0],
            home.sortBy[1],
            page,
            home.pageSize,
            home.currentVendorId,
            home.currentCategoryId,
            home.currentSearchKey
        );

        dispatch({
            type: aTypes.MOVE_PAGE_LAST,
            page,
            products
        });
    } catch (error) {
        console.log({ error });
    }
}

export const setCurrentCategoryId = (
    categoryId: string
): aTypes.SetCurrentCategoryIdAction => {
    return { type: aTypes.SET_CURRENT_CATEGORY_ID, categoryId }
}

export const setCurrentVendorId = (
    vendorId: string
): aTypes.SetCurrentVendorIdAction => {
    return { type: aTypes.SET_CURRENT_VENDOR_ID, vendorId }
}

export const setCurrentSearchKey = (
    searchKey: string
): aTypes.SetCurrentSearchKeyAction => {
    return { type: aTypes.SET_CURRENT_SEARCH_KEY, searchKey }
}

export const setSortBy = (sortType: string): aTypes.SetSortByAction => {
    return { type: aTypes.SET_SORT_BY, sortType }
}
