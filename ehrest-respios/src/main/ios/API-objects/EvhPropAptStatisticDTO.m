//
// EvhPropAptStatisticDTO.m
//
#import "EvhPropAptStatisticDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropAptStatisticDTO
//

@implementation EvhPropAptStatisticDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPropAptStatisticDTO* obj = [EvhPropAptStatisticDTO new];
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
    if(self.aptCount)
        [jsonObject setObject: self.aptCount forKey: @"aptCount"];
    if(self.familyCount)
        [jsonObject setObject: self.familyCount forKey: @"familyCount"];
    if(self.userCount)
        [jsonObject setObject: self.userCount forKey: @"userCount"];
    if(self.liveCount)
        [jsonObject setObject: self.liveCount forKey: @"liveCount"];
    if(self.rentCount)
        [jsonObject setObject: self.rentCount forKey: @"rentCount"];
    if(self.freeCount)
        [jsonObject setObject: self.freeCount forKey: @"freeCount"];
    if(self.decorateCount)
        [jsonObject setObject: self.decorateCount forKey: @"decorateCount"];
    if(self.unsaleCount)
        [jsonObject setObject: self.unsaleCount forKey: @"unsaleCount"];
    if(self.defaultCount)
        [jsonObject setObject: self.defaultCount forKey: @"defaultCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.aptCount = [jsonObject objectForKey: @"aptCount"];
        if(self.aptCount && [self.aptCount isEqual:[NSNull null]])
            self.aptCount = nil;

        self.familyCount = [jsonObject objectForKey: @"familyCount"];
        if(self.familyCount && [self.familyCount isEqual:[NSNull null]])
            self.familyCount = nil;

        self.userCount = [jsonObject objectForKey: @"userCount"];
        if(self.userCount && [self.userCount isEqual:[NSNull null]])
            self.userCount = nil;

        self.liveCount = [jsonObject objectForKey: @"liveCount"];
        if(self.liveCount && [self.liveCount isEqual:[NSNull null]])
            self.liveCount = nil;

        self.rentCount = [jsonObject objectForKey: @"rentCount"];
        if(self.rentCount && [self.rentCount isEqual:[NSNull null]])
            self.rentCount = nil;

        self.freeCount = [jsonObject objectForKey: @"freeCount"];
        if(self.freeCount && [self.freeCount isEqual:[NSNull null]])
            self.freeCount = nil;

        self.decorateCount = [jsonObject objectForKey: @"decorateCount"];
        if(self.decorateCount && [self.decorateCount isEqual:[NSNull null]])
            self.decorateCount = nil;

        self.unsaleCount = [jsonObject objectForKey: @"unsaleCount"];
        if(self.unsaleCount && [self.unsaleCount isEqual:[NSNull null]])
            self.unsaleCount = nil;

        self.defaultCount = [jsonObject objectForKey: @"defaultCount"];
        if(self.defaultCount && [self.defaultCount isEqual:[NSNull null]])
            self.defaultCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
