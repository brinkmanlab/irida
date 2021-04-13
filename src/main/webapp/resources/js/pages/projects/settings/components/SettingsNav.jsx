import { Link } from "@reach/router";
import { Menu } from "antd";
import React from "react";

/**
 * Component to handle navigation within the project settings page
 * @param {string} path - the current page name
 * @returns {JSX.Element}
 * @constructor
 */
export default function SettingsNav({ path }) {
  return (
    <Menu selectedKeys={[path]} style={{ height: `100%` }}>
      <Menu.Item key="details">
        <Link to="details">{i18n("project.settings.page.details")}</Link>
      </Menu.Item>
      <Menu.Item key="processing">
        <Link to="processing">{i18n("project.settings.page.processing")}</Link>
      </Menu.Item>
      <Menu.Item key="members">
        <Link to="members">{i18n("project.settings.page.members")}</Link>
      </Menu.Item>
    </Menu>
  );
}
