//
// EvhListFamilyBillsAndPaysByFamilyIdCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFamilyBillsAndPaysByFamilyIdCommandResponse
//
@interface EvhListFamilyBillsAndPaysByFamilyIdCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* billDate;

@property(nonatomic, copy) NSNumber* nextPageOffset;

@property(nonatomic, copy) NSString* orgTelephone;

@property(nonatomic, copy) NSString* orgName;

@property(nonatomic, copy) NSString* zlTelephone;

@property(nonatomic, copy) NSString* zlName;

@property(nonatomic, copy) NSNumber* orgIsExist;

// item type EvhPmBillsDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

