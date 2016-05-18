//
// EvhActivityListResponse.m
//
#import "EvhActivityListResponse.h"
#import "EvhActivityDTO.h"
#import "EvhActivityMemberDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityListResponse
//

@implementation EvhActivityListResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityListResponse* obj = [EvhActivityListResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _roster = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.activity) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.activity toJson: dic];
        
        [jsonObject setObject: dic forKey: @"activity"];
    }
    if(self.creatorFlag)
        [jsonObject setObject: self.creatorFlag forKey: @"creatorFlag"];
    if(self.checkinQRUrl)
        [jsonObject setObject: self.checkinQRUrl forKey: @"checkinQRUrl"];
    if(self.roster) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhActivityMemberDTO* item in self.roster) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"roster"];
    }
    if(self.nextAnchor)
        [jsonObject setObject: self.nextAnchor forKey: @"nextAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"activity"];

        self.activity = [EvhActivityDTO new];
        self.activity = [self.activity fromJson: itemJson];
        self.creatorFlag = [jsonObject objectForKey: @"creatorFlag"];
        if(self.creatorFlag && [self.creatorFlag isEqual:[NSNull null]])
            self.creatorFlag = nil;

        self.checkinQRUrl = [jsonObject objectForKey: @"checkinQRUrl"];
        if(self.checkinQRUrl && [self.checkinQRUrl isEqual:[NSNull null]])
            self.checkinQRUrl = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"roster"];
            for(id itemJson in jsonArray) {
                EvhActivityMemberDTO* item = [EvhActivityMemberDTO new];
                
                [item fromJson: itemJson];
                [self.roster addObject: item];
            }
        }
        self.nextAnchor = [jsonObject objectForKey: @"nextAnchor"];
        if(self.nextAnchor && [self.nextAnchor isEqual:[NSNull null]])
            self.nextAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
