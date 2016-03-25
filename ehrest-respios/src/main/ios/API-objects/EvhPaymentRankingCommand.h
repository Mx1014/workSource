//
// EvhPaymentRankingCommand.h
// generated at 2016-03-25 09:26:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentRankingCommand
//
@interface EvhPaymentRankingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* payStatus;

@property(nonatomic, copy) NSString* orderNo;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

