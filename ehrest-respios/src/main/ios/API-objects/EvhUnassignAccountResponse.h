//
// EvhUnassignAccountResponse.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUnassignAccountResponse
//
@interface EvhUnassignAccountResponse
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* accountIds;

@property(nonatomic, copy) NSNumber* accountsCount;

@property(nonatomic, copy) NSNumber* unassignAccountsCount;

@property(nonatomic, copy) NSNumber* expiredDate;

@property(nonatomic, copy) NSNumber* confType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

