//
// EvhRentalBillCountDTO.m
//
#import "EvhRentalBillCountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBillCountDTO
//

@implementation EvhRentalBillCountDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalBillCountDTO* obj = [EvhRentalBillCountDTO new];
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
    if(self.siteName)
        [jsonObject setObject: self.siteName forKey: @"siteName"];
    if(self.sumCount)
        [jsonObject setObject: self.sumCount forKey: @"sumCount"];
    if(self.completeCount)
        [jsonObject setObject: self.completeCount forKey: @"completeCount"];
    if(self.cancelCount)
        [jsonObject setObject: self.cancelCount forKey: @"cancelCount"];
    if(self.overTimeCount)
        [jsonObject setObject: self.overTimeCount forKey: @"overTimeCount"];
    if(self.successCount)
        [jsonObject setObject: self.successCount forKey: @"successCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.siteName = [jsonObject objectForKey: @"siteName"];
        if(self.siteName && [self.siteName isEqual:[NSNull null]])
            self.siteName = nil;

        self.sumCount = [jsonObject objectForKey: @"sumCount"];
        if(self.sumCount && [self.sumCount isEqual:[NSNull null]])
            self.sumCount = nil;

        self.completeCount = [jsonObject objectForKey: @"completeCount"];
        if(self.completeCount && [self.completeCount isEqual:[NSNull null]])
            self.completeCount = nil;

        self.cancelCount = [jsonObject objectForKey: @"cancelCount"];
        if(self.cancelCount && [self.cancelCount isEqual:[NSNull null]])
            self.cancelCount = nil;

        self.overTimeCount = [jsonObject objectForKey: @"overTimeCount"];
        if(self.overTimeCount && [self.overTimeCount isEqual:[NSNull null]])
            self.overTimeCount = nil;

        self.successCount = [jsonObject objectForKey: @"successCount"];
        if(self.successCount && [self.successCount isEqual:[NSNull null]])
            self.successCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
