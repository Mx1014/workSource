//
// EvhUpdatePmBillCommand.m
//
#import "EvhUpdatePmBillCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePmBillCommand
//

@implementation EvhUpdatePmBillCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdatePmBillCommand* obj = [EvhUpdatePmBillCommand new];
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
    if(self.organizationId)
        [jsonObject setObject: self.organizationId forKey: @"organizationId"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.startDate)
        [jsonObject setObject: self.startDate forKey: @"startDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.payDate)
        [jsonObject setObject: self.payDate forKey: @"payDate"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.dueAmount)
        [jsonObject setObject: self.dueAmount forKey: @"dueAmount"];
    if(self.oweAmount)
        [jsonObject setObject: self.oweAmount forKey: @"oweAmount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.organizationId = [jsonObject objectForKey: @"organizationId"];
        if(self.organizationId && [self.organizationId isEqual:[NSNull null]])
            self.organizationId = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.startDate = [jsonObject objectForKey: @"startDate"];
        if(self.startDate && [self.startDate isEqual:[NSNull null]])
            self.startDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.payDate = [jsonObject objectForKey: @"payDate"];
        if(self.payDate && [self.payDate isEqual:[NSNull null]])
            self.payDate = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.dueAmount = [jsonObject objectForKey: @"dueAmount"];
        if(self.dueAmount && [self.dueAmount isEqual:[NSNull null]])
            self.dueAmount = nil;

        self.oweAmount = [jsonObject objectForKey: @"oweAmount"];
        if(self.oweAmount && [self.oweAmount isEqual:[NSNull null]])
            self.oweAmount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
