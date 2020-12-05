import React, { useEffect, useState } from "react"
import Card from "../../components/Cards/Card/Card"
import CardBoard from "../../components/Cards/CardBoard/CardBoard"
import { useDispatch, useSelector } from "react-redux"
import {
    findAllCategories,
    findAllProducts,
    findAllVendors,
    movePageFirst,
    movePageLast,
    movePageLeft,
    movePageRight,
    setCurrentCategoryId,
    setCurrentSearchKey,
    setCurrentVendorId,
    setSortBy
} from "../../store/Home/actions/actions"
import { RootState } from "../../store/rootReducer"
import classes from "./Home.module.css"
import Category from "../../store/Home/model/Category"
import Vendor from "../../store/Home/model/Vendor"
import MenuToggle from "../../components/Menus/MenuToggle/MenuToggle"
import Menu from "../../components/Menus/Menu/Menu"
import MenuItem from "../../components/Menus/MenuItem/MenuItem"
import Backdrop from "../../components/Backdrop/Backdrop"
import * as Scenes from "../scenes/scenes"
import { setScene } from "../../store/Main/actions/actions"

const Home = () => {
    const [menuIsVisible, setMenuIsVisible] = useState(false)
    const dispatch = useDispatch()
    const currentPage = useSelector(
        (state: RootState) => state.home.currentPage
    )
    const pageSize = useSelector((state: RootState) => state.home.pageSize)
    const currentSortType = useSelector((state: RootState) => state.home.sortBy)
    const products = useSelector((state: RootState) => state.home.products)
    const categories = useSelector((state: RootState) => state.home.categories)
    const vendors = useSelector((state: RootState) => state.home.vendors)
    const currentCategoryId = useSelector(
        (state: RootState) => state.home.currentCategoryId
    )
    const currentVendorId = useSelector(
        (state: RootState) => state.home.currentVendorId
    )
    const currentSearchKey = useSelector(
        (state: RootState) => state.home.currentSearchKey
    )

    useEffect(() => {
        dispatch(findAllVendors())
        dispatch(findAllCategories())
    }, [])

    useEffect(() => {
        dispatch(findAllProducts())
    }, [
        pageSize,
        currentSortType,
        currentVendorId,
        currentCategoryId,
        currentSearchKey
    ])

    const cards = products.map((product) => {
        let vendor: Vendor | null = null
        for (let i = 0; i < vendors.length; i++) {
            if (vendors[i].id === product.vendorId) {
                vendor = vendors[i]
                break
            }
        }
        let category: Category | null = null
        for (let i = 0; i < categories.length; i++) {
            if (categories[i].id === product.categoryId) {
                category = categories[i]
                break
            }
        }
        return (
            <Card title={product.name} key={product.id}>
                <small>
                    {vendor
                        ? `by ${vendor.firstName} ${vendor.lastName}`
                        : null}
                </small>
                <div
                    style={{
                        display: "flex",
                        flexFlow: "row",
                        alignItems: "end",
                        justifyContent: "space-between"
                    }}
                >
                    <span
                        style={{
                            margin: "0",
                            color: "#cc7700"
                        }}
                    >{`$ ${Number.parseFloat(product.price).toFixed(2)}`}</span>
                    <span
                        style={{
                            fontSize: "0.75rem",
                            color: "#00cc77"
                        }}
                    >
                        {category ? `${category.name}` : null}
                    </span>
                </div>
            </Card>
        )
    })

    const pageRight = () => dispatch(movePageRight())
    const pageLeft = () => dispatch(movePageLeft())
    const pageFirst = () => dispatch(movePageFirst())
    const pageLast = () => dispatch(movePageLast())
    const sortBy = (newSortType: string) => dispatch(setSortBy(newSortType))

    const filterOptions: JSX.Element[] = [
        <option key="null" value="">
            Filter by...
        </option>
    ]

    const vendorGroup = vendors.map((vendor) => {
        return (
            <option
                key={vendor.id}
                value={`vendor ${vendor.id}`}
            >{`${vendor.firstName} ${vendor.lastName}`}</option>
        )
    })

    filterOptions.push(
        <optgroup key="vendors" label="Vendor">
            {vendorGroup}
        </optgroup>
    )

    const categoryGroup = categories.map((category) => {
        return (
            <option key={category.id} value={`category ${category.id}`}>
                {category.name}
            </option>
        )
    })

    filterOptions.push(
        <optgroup key="categories" label="Category">
            {categoryGroup}
        </optgroup>
    )

    const toggleMenu = () => () => setMenuIsVisible(!menuIsVisible)

    let output: JSX.Element
    output = (
        <>
            {menuIsVisible ? <Backdrop clicked={toggleMenu()} /> : null}
            <div
                style={{
                    display: "flex",
                    flexFlow: "row",
                    flexWrap: "wrap",
                    padding: "0 0.5rem",
                    backgroundColor: "#001144",
                    boxShadow: "0 0 2px 1px #001144"
                }}
            >
                <MenuToggle clicked={toggleMenu()} />
                <Menu
                    isVisible={menuIsVisible}
                    menuItems={[
                        <MenuItem
                            key="abcd"
                            clicked={() =>
                                dispatch(setScene(Scenes.ADD_PRODUCT))
                            }
                        >
                            Add new product
                        </MenuItem>
                    ]}
                />
            </div>
            <div
                style={{
                    display: "flex",
                    gap: "1rem",
                    flexDirection: "row",
                    flexWrap: "wrap"
                }}
            >
                <input
                    className={classes.Search}
                    defaultValue={currentSearchKey}
                    type="text"
                    placeholder="Search..."
                    autoFocus
                    onChange={(event) =>
                        dispatch(setCurrentSearchKey(event.currentTarget.value))
                    }
                    onKeyPress={(event) => {
                        if (event.key === "Enter") {
                            dispatch(
                                setCurrentSearchKey(event.currentTarget.value)
                            )
                        }
                    }}
                />
                <select
                    onChange={(event) => {
                        const s = event.currentTarget.value.split(" ")
                        if (s[0] === "vendor") {
                            dispatch(setCurrentCategoryId(""))
                            dispatch(setCurrentVendorId(s[1]))
                        } else if (s[0] === "category") {
                            dispatch(setCurrentVendorId(""))
                            dispatch(setCurrentCategoryId(s[1]))
                        } else {
                            dispatch(setCurrentVendorId(""))
                            dispatch(setCurrentCategoryId(""))
                        }
                    }}
                    className={classes.Select}
                >
                    {filterOptions}
                </select>
                <select
                    onChange={(event) => sortBy(event.currentTarget.value)}
                    className={classes.Select}
                >
                    <option value="id asc">Sort by...</option>
                    <option value="name asc">Name ascending</option>
                    <option value="name des">Name descending</option>
                    <option value="price asc">Price ascending</option>
                    <option value="price des">Price descending</option>
                </select>
            </div>
            <CardBoard
                cards={cards}
                pageLeft={pageLeft}
                pageRight={pageRight}
                pageFirst={pageFirst}
                pageLast={pageLast}
                currentPage={currentPage}
            />
        </>
    )
    return output
}

export default Home
