//
// EvhDeviceMessages.m
//
#import "EvhDeviceMessages.h"
#import "EvhDeviceMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeviceMessages
//

@implementation EvhDeviceMessages

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhDeviceMessages* obj = [EvhDeviceMessages new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _messages = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.messages) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhDeviceMessage* item in self.messages) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"messages"];
    }
    if(self.anchor)
        [jsonObject setObject: self.anchor forKey: @"anchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"messages"];
            for(id itemJson in jsonArray) {
                EvhDeviceMessage* item = [EvhDeviceMessage new];
                
                [item fromJson: itemJson];
                [self.messages addObject: item];
            }
        }
        self.anchor = [jsonObject objectForKey: @"anchor"];
        if(self.anchor && [self.anchor isEqual:[NSNull null]])
            self.anchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
