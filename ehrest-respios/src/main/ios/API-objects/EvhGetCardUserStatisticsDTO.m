//
// EvhGetCardUserStatisticsDTO.m
//
#import "EvhGetCardUserStatisticsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCardUserStatisticsDTO
//

@implementation EvhGetCardUserStatisticsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetCardUserStatisticsDTO* obj = [EvhGetCardUserStatisticsDTO new];
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
    if(self.totalCount)
        [jsonObject setObject: self.totalCount forKey: @"totalCount"];
    if(self.cardUserCount)
        [jsonObject setObject: self.cardUserCount forKey: @"cardUserCount"];
    if(self.normalUserCount)
        [jsonObject setObject: self.normalUserCount forKey: @"normalUserCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.totalCount = [jsonObject objectForKey: @"totalCount"];
        if(self.totalCount && [self.totalCount isEqual:[NSNull null]])
            self.totalCount = nil;

        self.cardUserCount = [jsonObject objectForKey: @"cardUserCount"];
        if(self.cardUserCount && [self.cardUserCount isEqual:[NSNull null]])
            self.cardUserCount = nil;

        self.normalUserCount = [jsonObject objectForKey: @"normalUserCount"];
        if(self.normalUserCount && [self.normalUserCount isEqual:[NSNull null]])
            self.normalUserCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
