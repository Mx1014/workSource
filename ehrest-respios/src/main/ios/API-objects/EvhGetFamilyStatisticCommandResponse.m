//
// EvhGetFamilyStatisticCommandResponse.m
//
#import "EvhGetFamilyStatisticCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetFamilyStatisticCommandResponse
//

@implementation EvhGetFamilyStatisticCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetFamilyStatisticCommandResponse* obj = [EvhGetFamilyStatisticCommandResponse new];
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
    if(self.totalDueOweAmount)
        [jsonObject setObject: self.totalDueOweAmount forKey: @"totalDueOweAmount"];
    if(self.totalPaidAmount)
        [jsonObject setObject: self.totalPaidAmount forKey: @"totalPaidAmount"];
    if(self.nowWaitPayAmount)
        [jsonObject setObject: self.nowWaitPayAmount forKey: @"nowWaitPayAmount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.totalDueOweAmount = [jsonObject objectForKey: @"totalDueOweAmount"];
        if(self.totalDueOweAmount && [self.totalDueOweAmount isEqual:[NSNull null]])
            self.totalDueOweAmount = nil;

        self.totalPaidAmount = [jsonObject objectForKey: @"totalPaidAmount"];
        if(self.totalPaidAmount && [self.totalPaidAmount isEqual:[NSNull null]])
            self.totalPaidAmount = nil;

        self.nowWaitPayAmount = [jsonObject objectForKey: @"nowWaitPayAmount"];
        if(self.nowWaitPayAmount && [self.nowWaitPayAmount isEqual:[NSNull null]])
            self.nowWaitPayAmount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
