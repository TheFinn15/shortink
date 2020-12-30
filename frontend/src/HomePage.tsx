import React from 'react';
import './App.css';
import {Avatar, Card, CardContent, CardHeader, IconButton, Typography} from '@material-ui/core';
import {createStyles, makeStyles, Theme} from '@material-ui/core/styles';
import {Person} from "@material-ui/icons";

// TODO: Сокращатель ссылок:
//  Полное управление ссылками и предпросмотр;
//  Мульти ссылки(скрещивание ссылок в одну, с возможностью перехода на каждую из них;
//  Лайф-лента ссылок;
//  Метод шифровки для ссылок и также мульти-ссылок.

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      margin: theme.spacing(10, 15, 0)
    },
    title: {
      margin: '0 2% 0',
      justifyContent: 'center',
      display: 'flex'
    }
  })
);

function getT(): any {
  let arr = []
  for (let i = 0; i < 5; i++) {
    arr.push(
      <Card style={{margin: 5+'%', width: 'auto'}}>
        <CardHeader avatar={
          <Avatar>
            {i}
          </Avatar>
        } action={
          <IconButton>
            <Person color="action"/>
          </IconButton>
        }>
        </CardHeader>
      </Card>
    )
  }
  return arr
}

function HomePage() {
  const styles = useStyles()
  return (
    <div>
      <Card className={styles.root}>
        <CardContent className={styles.title}>
          <Typography gutterBottom variant="h5">
            Лайф-лента ссылок
            {getT()}
          </Typography>
        </CardContent>
      </Card>
    </div>
  );
}

export default HomePage;
