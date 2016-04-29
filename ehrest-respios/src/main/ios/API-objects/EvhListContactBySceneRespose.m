//
// EvhListContactBySceneRespose.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import "EvhListContactBySceneRespose.h"
#import "EvhSceneContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactBySceneRespose
//

@implementation EvhListContactBySceneRespose

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListContactBySceneRespose* obj = [EvhListContactBySceneRespose new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _contacts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.contacts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSceneContactDTO* item in self.contacts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"contacts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"contacts"];
            for(id itemJson in jsonArray) {
                EvhSceneContactDTO* item = [EvhSceneContactDTO new];
                
                [item fromJson: itemJson];
                [self.contacts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
