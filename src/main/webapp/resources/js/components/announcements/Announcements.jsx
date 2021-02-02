import React, { useEffect, useState } from "react";
import { Card, Modal, notification, Tag, Typography } from "antd";
import {
  getUnreadAnnouncements,
  markAnnouncementRead,
} from "../../apis/announcements/announcements";
import Markdown from "react-markdown";
import { formatDate } from "../../utilities/date-utilities";

const { Text, Title } = Typography;

export function Announcements() {
  const [visible, setVisible] = useState(false);
  const [announcements, setAnnouncements] = useState();
  const [index, setIndex] = useState(0);

  useEffect(() => {
    getUnreadAnnouncements().then(({ data }) => {
      if (data.length) {
        setAnnouncements(data);
        setVisible(true);
      }
    });
  }, []);

  function markAnnouncementAsRead(aID) {
    markAnnouncementRead({ aID })
      .then(() => {
        setIndex(index + 1);
      })
      .catch(({ message }) => {
        notification.error({ message });
      });
  }

  const onOk = () => {
    if (index + 1 < announcements.length) {
      markAnnouncementAsRead(announcements[index].identifier);
    } else {
      setVisible(false);
    }
  };

  return announcements && visible ? (
    <Modal
      closable={false}
      title={
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          {`There are ${announcements.length} priority messages`}
          <Tag color="red">
            {index + 1} / {announcements.length}
          </Tag>
        </div>
      }
      visible={visible}
      width="60%"
      okText="Read"
      onOk={onOk}
      onCancel={() => setVisible(false)}
    >
      <Card
        title={
          <>
            <Title level={5}>{announcements[index].title}</Title>
            <Text type="secondary" style={{ fontSize: `.8em` }}>
              {i18n(
                "Announcements.create.details",
                announcements[index].user.username,
                formatDate({ date: announcements[index].createdDate })
              )}
            </Text>
          </>
        }
      >
        <div style={{ overflowY: "auto", maxHeight: 600 }}>
          <Markdown source={announcements[index].message} />
        </div>
      </Card>
    </Modal>
  ) : null;
}
