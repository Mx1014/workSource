//
// EvhNotifyDoorMessageResponse.m
//
#import "EvhNotifyDoorMessageResponse.h"
#import "EvhPhoneStatus.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNotifyDoorMessageResponse
//

@implementation EvhNotifyDoorMessageResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhNotifyDoorMessageResponse* obj = [EvhNotifyDoorMessageResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _phoneStatus = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.phoneStatus) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPhoneStatus* item in self.phoneStatus) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"phoneStatus"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"phoneStatus"];
            for(id itemJson in jsonArray) {
                EvhPhoneStatus* item = [EvhPhoneStatus new];
                
                [item fromJson: itemJson];
                [self.phoneStatus addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
