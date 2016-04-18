//
// EvhCommonOrderCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommonOrderCommand
//
@interface EvhCommonOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSNumber* totalFee;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* body;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

