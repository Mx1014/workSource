//
// EvhFindNewestBillByAddressIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindNewestBillByAddressIdCommand
//
@interface EvhFindNewestBillByAddressIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* addressId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

