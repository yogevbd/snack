"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.NavigationButton = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const __1 = require("../../");
const EventsStore_1 = require("../Stores/EventsStore");
exports.NavigationButton = class extends react_1.Component {
    constructor() {
        super(...arguments);
        this.ref = undefined;
    }
    render() {
        const { button, componentId } = this.props;
        if (button.component)
            return this.renderButtonComponent();
        return (react_1.default.createElement(react_native_1.Button, { testID: button.testID, key: button.id, title: button.text || '', onPress: () => button.enabled !== false &&
                EventsStore_1.events.invokeNavigationButtonPressed({
                    buttonId: button.id,
                    componentId,
                }) }));
    }
    renderButtonComponent() {
        const { button, componentId } = this.props;
        //@ts-ignore
        const buttonComponentId = button.component.componentId;
        //@ts-ignore
        const Component = __1.Navigation.mock.store.getComponentClassForName(button.component.name)();
        const props = __1.Navigation.mock.store.getPropsForId(buttonComponentId);
        return (react_1.default.createElement(react_native_1.TouchableOpacity, { onPress: () => {
                if (this.ref) {
                    // @ts-ignore
                    this.invokeOnClick(this.ref._reactInternalFiber.return.stateNode);
                }
                EventsStore_1.events.invokeNavigationButtonPressed({
                    buttonId: button.id,
                    componentId: componentId,
                });
            }, testID: button.testID },
            react_1.default.createElement(Component, Object.assign({ key: buttonComponentId }, props, { componentId: buttonComponentId, ref: (ref) => (this.ref = ref) }))));
    }
    invokeOnClick(stateNode) {
        if (stateNode.children) {
            // @ts-ignore
            stateNode.children.forEach((instance) => {
                if (instance.internalInstanceHandle &&
                    instance.internalInstanceHandle.stateNode.props.onClick) {
                    instance.internalInstanceHandle.stateNode.props.onClick();
                }
                this.invokeOnClick(instance);
            });
        }
    }
};