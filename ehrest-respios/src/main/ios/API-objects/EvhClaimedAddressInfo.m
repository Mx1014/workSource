//
// EvhClaimedAddressInfo.m
//
#import "EvhClaimedAddressInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhClaimedAddressInfo
//

@implementation EvhClaimedAddressInfo

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhClaimedAddressInfo* obj = [EvhClaimedAddressInfo new];
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
    if(self.apartmentId)
        [jsonObject setObject: self.apartmentId forKey: @"apartmentId"];
    if(self.fullAddress)
        [jsonObject setObject: self.fullAddress forKey: @"fullAddress"];
    if(self.userCount)
        [jsonObject setObject: self.userCount forKey: @"userCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.apartmentId = [jsonObject objectForKey: @"apartmentId"];
        if(self.apartmentId && [self.apartmentId isEqual:[NSNull null]])
            self.apartmentId = nil;

        self.fullAddress = [jsonObject objectForKey: @"fullAddress"];
        if(self.fullAddress && [self.fullAddress isEqual:[NSNull null]])
            self.fullAddress = nil;

        self.userCount = [jsonObject objectForKey: @"userCount"];
        if(self.userCount && [self.userCount isEqual:[NSNull null]])
            self.userCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
