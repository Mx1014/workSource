//
// EvhSyncLocationCommand.m
//
#import "EvhSyncLocationCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncLocationCommand
//

@implementation EvhSyncLocationCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSyncLocationCommand* obj = [EvhSyncLocationCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.collectTimeMs)
        [jsonObject setObject: self.collectTimeMs forKey: @"collectTimeMs"];
    if(self.reportTimeMs)
        [jsonObject setObject: self.reportTimeMs forKey: @"reportTimeMs"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.collectTimeMs = [jsonObject objectForKey: @"collectTimeMs"];
        if(self.collectTimeMs && [self.collectTimeMs isEqual:[NSNull null]])
            self.collectTimeMs = nil;

        self.reportTimeMs = [jsonObject objectForKey: @"reportTimeMs"];
        if(self.reportTimeMs && [self.reportTimeMs isEqual:[NSNull null]])
            self.reportTimeMs = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
