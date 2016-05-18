//
// EvhApplyParkCardDTO.m
//
#import "EvhApplyParkCardDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApplyParkCardDTO
//

@implementation EvhApplyParkCardDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApplyParkCardDTO* obj = [EvhApplyParkCardDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.applierName)
        [jsonObject setObject: self.applierName forKey: @"applierName"];
    if(self.applierPhone)
        [jsonObject setObject: self.applierPhone forKey: @"applierPhone"];
    if(self.companyName)
        [jsonObject setObject: self.companyName forKey: @"companyName"];
    if(self.plateNumber)
        [jsonObject setObject: self.plateNumber forKey: @"plateNumber"];
    if(self.applyTime)
        [jsonObject setObject: self.applyTime forKey: @"applyTime"];
    if(self.applyStatus)
        [jsonObject setObject: self.applyStatus forKey: @"applyStatus"];
    if(self.fetchStatus)
        [jsonObject setObject: self.fetchStatus forKey: @"fetchStatus"];
    if(self.deadline)
        [jsonObject setObject: self.deadline forKey: @"deadline"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.applierName = [jsonObject objectForKey: @"applierName"];
        if(self.applierName && [self.applierName isEqual:[NSNull null]])
            self.applierName = nil;

        self.applierPhone = [jsonObject objectForKey: @"applierPhone"];
        if(self.applierPhone && [self.applierPhone isEqual:[NSNull null]])
            self.applierPhone = nil;

        self.companyName = [jsonObject objectForKey: @"companyName"];
        if(self.companyName && [self.companyName isEqual:[NSNull null]])
            self.companyName = nil;

        self.plateNumber = [jsonObject objectForKey: @"plateNumber"];
        if(self.plateNumber && [self.plateNumber isEqual:[NSNull null]])
            self.plateNumber = nil;

        self.applyTime = [jsonObject objectForKey: @"applyTime"];
        if(self.applyTime && [self.applyTime isEqual:[NSNull null]])
            self.applyTime = nil;

        self.applyStatus = [jsonObject objectForKey: @"applyStatus"];
        if(self.applyStatus && [self.applyStatus isEqual:[NSNull null]])
            self.applyStatus = nil;

        self.fetchStatus = [jsonObject objectForKey: @"fetchStatus"];
        if(self.fetchStatus && [self.fetchStatus isEqual:[NSNull null]])
            self.fetchStatus = nil;

        self.deadline = [jsonObject objectForKey: @"deadline"];
        if(self.deadline && [self.deadline isEqual:[NSNull null]])
            self.deadline = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
